/*
 * Copyright 2012-2013 inBloom, Inc. and its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.slc.sli.ingestion.processors;

import java.io.*;
import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.impl.DefaultProducerTemplate;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.slc.sli.ingestion.tenant.TenantDA;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import org.slc.sli.common.util.logging.LogLevelType;
import org.slc.sli.common.util.logging.SecurityEvent;
import org.slc.sli.common.util.tenantdb.TenantContext;
import org.slc.sli.ingestion.BatchJobStageType;
import org.slc.sli.ingestion.BatchJobStatusType;
import org.slc.sli.ingestion.FaultType;
import org.slc.sli.ingestion.FileFormat;
import org.slc.sli.ingestion.WorkNote;
import org.slc.sli.ingestion.model.Error;
import org.slc.sli.ingestion.model.Metrics;
import org.slc.sli.ingestion.model.NewBatchJob;
import org.slc.sli.ingestion.model.ResourceEntry;
import org.slc.sli.ingestion.model.Stage;
import org.slc.sli.ingestion.model.da.BatchJobDAO;
import org.slc.sli.ingestion.queues.MessageType;
import org.slc.sli.ingestion.util.BatchJobUtils;

/**
 * Writes out a job report and any errors/warnings associated with the job.
 *
 * @author bsuzuki
 *
 */
@Component
public class JobReportingProcessor implements Processor {

    public static final BatchJobStageType BATCH_JOB_STAGE = BatchJobStageType.JOB_REPORTING_PROCESSOR;

    private static final String BATCH_JOB_STAGE_DESC = "Writes out a job report and any errors/warnings associated with the job";

    public static final String JOB_STAGE_RESOURCE_ID = "job";

    private static final Logger LOG = LoggerFactory.getLogger(JobReportingProcessor.class);

    private static final List<String> ORCHESTRATION_STAGES_LIST = Arrays.asList("PersistenceProcessor",
            "TransformationProcessor", "WorkNoteSplitter");

    public static final String ORCHESTRATION_STAGES_NAME = "OrchestrationStages";

    public static final String ORCHESTRATION_STAGES_DESC = "Transforms and persists records to sli database";

    public static final String ERROR_FILE_TYPE = "error";
    public static final String WARNING_FILE_TYPE = "warn";

    @Value("${sli.ingestion.topic.command}")
    private String commandTopicUri;

    @Autowired
    private TenantDA tenantDA;

    @Autowired
    private BatchJobDAO batchJobDAO;

    @Value("${sli.ingestion.errorsCountPerInterchange}")
    private int errorsCountPerInterchange;

    @Value("${sli.ingestion.warningsCountPerInterchange}")
    private int warningsCountPerInterchange;

    @Override
    public void process(Exchange exchange) {
        WorkNote workNote = exchange.getIn().getBody(WorkNote.class);

        if (workNote == null || workNote.getBatchJobId() == null) {
            missingBatchJobIdError(exchange);
        } else {
            processJobReporting(exchange, workNote);
        }
    }

    private void processJobReporting(Exchange exchange, WorkNote workNote) {
        Stage stage = Stage.createAndStartStage(BATCH_JOB_STAGE, BATCH_JOB_STAGE_DESC);

        String batchJobId = workNote.getBatchJobId();
        NewBatchJob job = null;
        try {
            populateJobBriefFromStageCollection(batchJobId);

            job = batchJobDAO.findBatchJobById(batchJobId);
            TenantContext.setTenantId(job.getTenantId());

            boolean hasErrors = writeErrorAndWarningReports(job);

            writeBatchJobReportFile(exchange, job, hasErrors);

        } catch (Exception e) {
            LOG.error("Exception encountered in JobReportingProcessor. ", e);
        } finally {

            performJobCleanup(exchange, workNote, stage, job);

        }
    }

    private void populateJobBriefFromStageCollection(String jobId) {
        NewBatchJob job = batchJobDAO.findBatchJobById(jobId);

        Map<String, Stage> stageBriefMap = new HashMap<String, Stage>();

        if (job != null) {
            List<Stage> stages = batchJobDAO.getBatchJobStages(jobId);
            Iterator<Stage> it = stages.iterator();

            while (it.hasNext()) {
                Stage stageChunk = it.next();

                // Account for special case of orchestration stages.
                String stageName = ORCHESTRATION_STAGES_NAME;
                String stageDesc = ORCHESTRATION_STAGES_DESC;
                if (!ORCHESTRATION_STAGES_LIST.contains(stageChunk.getStageName())) {
                    stageName = stageChunk.getStageName();
                    stageDesc = stageChunk.getStageDesc();
                }

                Stage stageBrief = stageBriefMap.get(stageName);
                if (stageBrief != null) {
                    if (stageBrief.getStartTimestamp() != null
                            && stageBrief.getStartTimestamp().getTime() > stageChunk.getStartTimestamp().getTime()) {
                        stageBrief.setStartTimestamp(stageChunk.getStartTimestamp());
                    }
                    if (stageBrief.getStopTimestamp() != null
                            && stageBrief.getStopTimestamp().getTime() < stageChunk.getStopTimestamp().getTime()) {
                        stageBrief.setStopTimestamp(stageChunk.getStopTimestamp());
                    }
                    stageBrief.setElapsedTime(stageBrief.getStopTimestamp().getTime()
                            - stageBrief.getStartTimestamp().getTime());

                } else {
                    stageBrief = new Stage(stageName, stageDesc, stageChunk.getStatus(),
                            stageChunk.getStartTimestamp(), stageChunk.getStopTimestamp(), null);
                    stageBrief.setJobId(stageChunk.getJobId());
                    stageBrief.setElapsedTime(stageChunk.getElapsedTime());
                    stageBrief.setProcessingInformation("");

                    stageBriefMap.put(stageName, stageBrief);
                }
            }

            Iterator<Entry<String, Stage>> iter = stageBriefMap.entrySet().iterator();
            while (iter.hasNext()) {
                Entry<String, Stage> temp = iter.next();
                job.addStage(temp.getValue());
            }

            batchJobDAO.saveBatchJob(job);
        } else {
            LOG.warn("Couldn't find job {}", jobId);
        }
    }

    private void writeBatchJobReportFile(Exchange exchange, NewBatchJob job, boolean hasErrors) {

        PrintWriter jobReportWriter = null;
        try {
            File file = getLogFile(job);
            BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(file), 65536);
            jobReportWriter = new PrintWriter(outputStream);

            writeInfoLine(job, jobReportWriter, "jobId: " + job.getId());

            long recordsProcessed = writeBatchJobPersistenceMetrics(job, jobReportWriter);

            writeBatchJobProperties(job, jobReportWriter);

            writeDuplicates(job, jobReportWriter);
            if (!hasErrors) {
                writeInfoLine(job, jobReportWriter, "All records processed successfully.");
                job.setStatus(BatchJobStatusType.COMPLETED_SUCCESSFULLY.getName());
            } else {
                writeInfoLine(job, jobReportWriter, "Not all records were processed completely due to errors.");
                job.setStatus(BatchJobStatusType.COMPLETED_WITH_ERRORS.getName());
            }

            writeInfoLine(job, jobReportWriter, "Processed " + recordsProcessed + " records.");

            String purgeMessage = (String) exchange.getProperty("purge.complete");

            if (purgeMessage != null) {

                writeInfoLine(job, jobReportWriter, purgeMessage);

            }

        } catch (IOException e) {
            LOG.error("Unable to write report file for: {}", job.getId());
        } finally {
            cleanupWriter(jobReportWriter);
        }
    }

    private void writeBatchJobProperties(NewBatchJob job, PrintWriter jobReportWriter) {
        if (job.getBatchProperties() != null) {
            for (Entry<String, String> entry : job.getBatchProperties().entrySet()) {
                writeInfoLine(job, jobReportWriter, "[configProperty] " + entry.getKey() + ": " + entry.getValue());
            }
        }
    }

    private boolean writeErrorAndWarningReports(NewBatchJob job) {
        boolean hasErrors = false;

        for (ResourceEntry resource : job.getResourceEntries()) {
            hasErrors |= writeUserLog(job, resource, FaultType.TYPE_ERROR, errorsCountPerInterchange);

            writeUserLog(job, resource, FaultType.TYPE_WARNING, warningsCountPerInterchange);
        }

        //It is possible that some errors dont have resourceId.
        //Reporting such errors.
        hasErrors |= writeUserLog(job, null, FaultType.TYPE_ERROR, errorsCountPerInterchange);

        return hasErrors;
    }

    private boolean writeUserLog(NewBatchJob job, ResourceEntry resource, FaultType severity, int logRecordsLimit) {
        String resourceId = resource != null ? resource.getResourceId() : null;

        Iterable<Error> errors = batchJobDAO.getBatchJobErrors(job.getId(), resourceId, severity, logRecordsLimit);

        if (errors.iterator().hasNext()) {
            PrintWriter errorWriter = null;
            try {
                String fileType = FaultType.TYPE_ERROR.equals(severity) ? ERROR_FILE_TYPE : WARNING_FILE_TYPE;
                errorWriter = getErrorWriter(fileType, job, resource);
                for (Error error : errors) {
                    writeErrorLine(errorWriter, severity.getName(), error.getErrorDetail());
                }
            } catch (IOException e) {
                LOG.error("Unable to write error file for: {}", job.getId(), e);
            } finally {
                cleanupWriter(errorWriter);
            }
            return true;
        }
        return false;
    }

    private PrintWriter getErrorWriter(String type, NewBatchJob job, ResourceEntry resource) throws IOException {
        String errorFileName = null;

        if (resource == null) {
            errorFileName = JOB_STAGE_RESOURCE_ID + "_" + type + "-" + job.getId() + ".log";
        } else {
            errorFileName = type + "." + resource.getResourceId() + "-" + job.getId() + ".log";
        }

        return new PrintWriter(new BufferedOutputStream(new FileOutputStream(createFile(job, errorFileName)), 65536));
    }

    /**
     * Returns a java.io.File for a log file to be used to report BatchJob
     * status/progress.  The file will be created if it does not yet exist.
     *
     * @return File object
     */
    public File getLogFile(NewBatchJob job) throws IOException {
        String fileName = "job-" + job.getId() + ".log";
        return createFile(job, fileName);
    }

    /**
     * @param job
     * @param fileName
     * @return
     * @throws IOException
     */
    public File createFile(NewBatchJob job, String fileName) throws IOException {
        File f = FileUtils.getFile(job.getTopLevelSourceId(), fileName);
        if (!f.exists()) {
            f.createNewFile();
        }
        return f;
    }

    private void writeDuplicates(NewBatchJob job, PrintWriter jobReportWriter) {

        Map<String,Map<String, Long>> combinedMetrics = new HashMap<String, Map<String, Long>>();
        List<Metrics> deltaStageMetrics = new ArrayList<Metrics>();
        List<Stage> deltaStages = batchJobDAO.getBatchJobStages(job.getId(), BatchJobStageType.DELTA_PROCESSOR);
        for(Stage stage : deltaStages) {
            for(Metrics metric : stage.getMetrics()) {
                if(metric.getDuplicateCounts() != null) {
                    deltaStageMetrics.add(metric);
                }
            }
        }

        if(deltaStageMetrics != null) {
            for(Metrics metrics : deltaStageMetrics) {
                String resourceId = metrics.getResourceId();
                Map<String, Long> combinedDuplicateCounts = combinedMetrics.containsKey(resourceId) ? collateDuplicateCounts(combinedMetrics.get(resourceId), metrics.getDuplicateCounts()) : metrics.getDuplicateCounts();
                combinedMetrics.put(resourceId, combinedDuplicateCounts);
            }
        }

        for(Entry<String, Map<String, Long>> entry : combinedMetrics.entrySet()) {
            String resource = entry.getKey();
            Map<String, Long> duplicates = entry.getValue();
            if(duplicates != null) {
                for (Map.Entry<String, Long> dupEntry : duplicates.entrySet()) {
                  Long count = dupEntry.getValue();
                  if (count > 0) {
                      writeInfoLine(job, jobReportWriter, resource + " " + dupEntry.getKey() + " " + count
                              + " deltas!");
                  }
              }

            }
        }
    }

    private Map<String, Long> collateDuplicateCounts(Map<String, Long> currentCounts, Map<String, Long> metricsCount) {
        Map<String, Long> combinedCounts = new HashMap<String, Long>();
        combinedCounts.putAll(currentCounts);
        for(Map.Entry<String,Long> entry : metricsCount.entrySet()) {
            Long count = combinedCounts.containsKey(entry.getKey()) ? combinedCounts.get(entry.getKey()) + entry.getValue() : entry.getValue();
            combinedCounts.put(entry.getKey(),count);
        }
        return combinedCounts;
    }

    private long writeBatchJobPersistenceMetrics(NewBatchJob job, PrintWriter jobReportWriter) {
        long totalProcessed = 0;

        List<Stage> stages = batchJobDAO.getBatchJobStages(job.getId(), BatchJobStageType.PERSISTENCE_PROCESSOR);
        stages.addAll(batchJobDAO.getBatchJobStages(job.getId(), BatchJobStageType.EDFI_PARSER_PROCESSOR));

        Iterator<Stage> it = stages.iterator();

        Stage stage;
        List<Metrics> metrics;

        Map<String, Metrics> combinedMetricsMap = new HashMap<String, Metrics>();

        while (it.hasNext()) {
            stage = it.next();
            metrics = stage.getMetrics();

            for (Metrics m : metrics) {

                if (combinedMetricsMap.containsKey(m.getResourceId())) {
                    // metrics exists, we should aggregate
                    Metrics temp = new Metrics(m.getResourceId());

                    temp.setResourceId(combinedMetricsMap.get(m.getResourceId()).getResourceId());
                    temp.setRecordCount(combinedMetricsMap.get(m.getResourceId()).getRecordCount());
                    temp.setErrorCount(combinedMetricsMap.get(m.getResourceId()).getErrorCount());
                    temp.setValidationErrorCount(combinedMetricsMap.get(m.getResourceId()).getValidationErrorCount());
                    temp.setDeletedCount(combinedMetricsMap.get(m.getResourceId()).getDeletedCount());
                    temp.setDeletedChildCount(combinedMetricsMap.get(m.getResourceId()).getDeletedChildCount());

                    temp.setDeletedChildCount(temp.getDeletedChildCount() + m.getDeletedChildCount());
                    temp.setDeletedCount(temp.getDeletedCount() + m.getDeletedCount());
                    temp.setErrorCount(temp.getErrorCount() + m.getErrorCount());
                    temp.setRecordCount(temp.getRecordCount() + m.getRecordCount());
                    temp.setValidationErrorCount(temp.getValidationErrorCount() + m.getValidationErrorCount());

                    combinedMetricsMap.put(m.getResourceId(), temp);

                } else {
                    // adding metrics to the map
                    Metrics aggregatedMetrics = new Metrics(m.getResourceId(), m.getRecordCount(), m.getErrorCount(), m.getDeletedCount(), m.getDeletedChildCount());
                    aggregatedMetrics.setValidationErrorCount(m.getValidationErrorCount());
                    combinedMetricsMap.put(m.getResourceId(), aggregatedMetrics);
                }

            }
        }

        Collection<Metrics> combinedMetrics = combinedMetricsMap.values();

        for (Metrics metric : combinedMetrics) {
            ResourceEntry resourceEntry = job.getResourceEntry(metric.getResourceId());
            if (resourceEntry == null) {
                LOG.error("The resource referenced by metric by resourceId " + metric.getResourceId()
                        + " is not defined for this job");
                continue;
            }

            logResourceMetric(job, resourceEntry, metric.getRecordCount(), metric.getErrorCount(), metric.getValidationErrorCount(),
                    metric.getDeletedCount(), metric.getDeletedChildCount(), jobReportWriter);

            totalProcessed += metric.getRecordCount();
            totalProcessed += metric.getValidationErrorCount();

            // update resource entries for zero-count reporting later
            resourceEntry.setRecordCount(metric.getRecordCount());
            resourceEntry.setErrorCount(metric.getErrorCount());
            resourceEntry.setValidationErrorCount(metric.getValidationErrorCount());
        }

        writeZeroCountPersistenceMetrics(job, jobReportWriter);

        return totalProcessed;
    }

    private void writeZeroCountPersistenceMetrics(NewBatchJob job, PrintWriter jobReportWriter) {
        // write out 0 count metrics for the input files

        for (ResourceEntry resourceEntry : job.getResourceEntries()) {
            if (resourceEntry.getResourceFormat() != null
                    && resourceEntry.getResourceFormat().equalsIgnoreCase(FileFormat.EDFI_XML.getCode())
                    && resourceEntry.getRecordCount() == 0 && resourceEntry.getErrorCount() == 0 && resourceEntry.getValidationErrorCount() == 0) {
                logResourceMetric(job, resourceEntry, 0, 0, 0, 0, 0, jobReportWriter);
            }
        }
    }

    private void logResourceMetric(NewBatchJob job, ResourceEntry resourceEntry, long numProcessed, long numFailed, long numFailedValidation,
            long numDeleted, long numDeletedChild, PrintWriter jobReportWriter) {

        String id = "[file] " + resourceEntry.getExternallyUploadedResourceId();
        writeInfoLine(job, jobReportWriter,
                id + " (" + resourceEntry.getResourceFormat() + "/" + resourceEntry.getResourceType() + ")");

        long numPassed = numProcessed - numFailed - numDeleted;

        writeInfoLine(job, jobReportWriter, id + " records considered for processing: " + numProcessed);
        writeInfoLine(job, jobReportWriter, id + " records ingested successfully: " + numPassed);
        writeInfoLine(job, jobReportWriter, id + " records deleted successfully: " + numDeleted);
        // TODO uncomment if/when cascade deletes are supported
//        writeInfoLine(job, jobReportWriter, id + " child records deleted successfully: " + numDeletedChild);
        writeInfoLine(job, jobReportWriter, id + " records failed processing: " + numFailed);
        writeInfoLine(job, jobReportWriter, id + " records not considered for processing: " + numFailedValidation);
    }

    private void missingBatchJobIdError(Exchange exchange) {
        exchange.getIn().setHeader("IngestionMessageType", MessageType.ERROR.name());
        LOG.error("No BatchJobId specified in " + this.getClass().getName() + " exchange message.");
    }

    private void writeInfoLine(NewBatchJob job, PrintWriter jobReportWriter, String string) {
        writeLine(jobReportWriter, "INFO", string);
        writeSecurityLog(job, LogLevelType.TYPE_INFO, string);
    }

    private void writeErrorLine(PrintWriter jobReportWriter, String severity, String string) {
        writeLine(jobReportWriter, severity, string);
    }

    private void writeLine(PrintWriter jobReportWriter, String type, String text) {
        jobReportWriter.write(type + "  " + text);
        jobReportWriter.println();
    }

    private void cleanupWriter(PrintWriter reportWriter) {
        if (reportWriter != null) {
            reportWriter.flush();
            reportWriter.close();
        }
    }

    private void performJobCleanup(Exchange exchange, WorkNote workNote, Stage stage, NewBatchJob job) {
        if (job != null) {
            BatchJobUtils.completeStageAndJob(stage, job);
            batchJobDAO.saveBatchJob(job);
            broadcastFlushStats(exchange, workNote);
            cleanUpLZ(job);
        }

        TenantContext.setJobId(null);
    }

    private void writeSecurityLog(NewBatchJob job, LogLevelType messageType, String message) {
        byte[] ipAddr = null;
        try {
            InetAddress addr = InetAddress.getLocalHost();

            // Get IP Address
            ipAddr = addr.getAddress();

        } catch (UnknownHostException e) {
            LOG.error("Error getting local host", e);
        }

        String edOrg = tenantDA.getTenantEdOrg(job.getTopLevelSourceId());
        if ( edOrg == null ) {
			edOrg = "";
		}

        List<String> userRoles = Collections.emptyList();
        SecurityEvent event = new SecurityEvent();
        event.setTenantId(""); // Alpha MH (tenantId - written in 'message')
        event.setUser("");
        event.setUserEdOrg(edOrg);
        event.setTargetEdOrgList(edOrg); //@TA10431 - change targetEdOrg from scalar to list
        event.setActionUri("writeLine");
        event.setAppId("Ingestion");
        event.setOrigin("");
        if (ipAddr != null) {
            event.setExecutedOn(ipAddr[0] + "." + ipAddr[1] + "." + ipAddr[2] + "." + ipAddr[3]);
        }
        event.setCredential("");
        event.setUserOrigin("");
        event.setTimeStamp(new Date());
        event.setProcessNameOrId(ManagementFactory.getRuntimeMXBean().getName());
        event.setClassName(this.getClass().getName());
        event.setLogLevel(messageType);
        event.setRoles(userRoles);
        event.setLogMessage(message);
        audit(event);
    }

    /**
     * broadcast a message to all orchestra nodes to flush their execution stats
     *
     * @param exchange
     * @param workNote
     */
    private void broadcastFlushStats(Exchange exchange, WorkNote workNote) {
        try {
            ProducerTemplate template = new DefaultProducerTemplate(exchange.getContext());
            template.start();
            template.sendBody(this.commandTopicUri, "jobCompleted|" + workNote.getBatchJobId());
            template.stop();
        } catch (Exception e) {
            LOG.error("Error sending `that's all folks` message to the orchestra", e);
        }
    }

    private void cleanUpLZ(NewBatchJob job) {
            String sourceId = job.getSourceId();
            if (sourceId != null) {
                File dir = new File(sourceId);
                FileUtils.deleteQuietly(dir);
            }
    }

    public void setCommandTopicUri(String commandTopicUri) {
        this.commandTopicUri = commandTopicUri;
    }

    public void setBatchJobDAO(BatchJobDAO batchJobDAO) {
        this.batchJobDAO = batchJobDAO;
    }

    public void setTenantDA(TenantDA tenantDA) {
    	this.tenantDA = tenantDA;
    }
}
