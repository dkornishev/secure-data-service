/*
 * Copyright 2012 Shared Learning Collaborative, LLC
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
package org.slc.sli.ingestion.routes.orchestra;

import java.util.ArrayList;
import java.util.List;

import org.apache.camel.Exchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import org.slc.sli.common.util.tenantdb.TenantContext;
import org.slc.sli.ingestion.ResourceEntryWorkNote;
import org.slc.sli.ingestion.WorkNote;
import org.slc.sli.ingestion.dal.NeutralRecordAccess;
import org.slc.sli.ingestion.model.NewBatchJob;
import org.slc.sli.ingestion.model.da.BatchJobDAO;
import org.slc.sli.ingestion.util.BatchJobUtils;

/**
 * Splits the zip file, and generate a FileEntryWorkNote for each file
 * @author tke
 *
 */
@Component
public class ZipFileSplitter {
    private static final Logger LOG = LoggerFactory.getLogger(ZipFileSplitter.class);
    public static final String ZIP_FILE_SPLITTER = "Splits the zip file into FileEntryWorkNote per file within the zip file";

    @Autowired
    private BatchJobDAO batchJobDAO;

    @Autowired
    private NeutralRecordAccess neutralRecordMongoAccess;

    public List<ResourceEntryWorkNote> splitZipFile(Exchange exchange) {
        String jobId = null;
        List<ResourceEntryWorkNote> resourceEntryWorkNotes = null;

        WorkNote workNote = exchange.getIn().getBody(WorkNote.class);
        jobId = workNote.getBatchJobId();

        TenantContext.setJobId(jobId);
        LOG.info("splitting zip file for job {}", jobId);

        indexStagingDB();

        NewBatchJob newBatchJob = batchJobDAO.findBatchJobById(jobId);
        List<String> resourceIds = newBatchJob.getResourceIds();
        resourceEntryWorkNotes = createWorkNotes(jobId, resourceIds, workNote.hasErrors());

        return resourceEntryWorkNotes;
    }

    /**
     * @param jobId
     * @param zipFile
     * @return
     */
    private List<ResourceEntryWorkNote> createWorkNotes (String jobId, List<String> fileEntries, boolean hasErrors) {
        List<ResourceEntryWorkNote> fileEntryWorkNotes = new ArrayList<ResourceEntryWorkNote>();
        List<String> fileNames = new ArrayList<String>();

        for (String fileEntry : fileEntries) {
            fileNames.add(fileEntry);
            fileEntryWorkNotes.add(new ResourceEntryWorkNote(jobId, "SLI", fileEntry, hasErrors));
        }

        batchJobDAO.createFileLatch(jobId, fileNames);

        return fileEntryWorkNotes;
    }

    private void indexStagingDB() {
        String jobId = TenantContext.getJobId();
        String dbName = BatchJobUtils.jobIdToDbName(jobId);

        LOG.info("Indexing staging db {} for job {}", dbName, jobId);
        neutralRecordMongoAccess.ensureIndexes();
    }

    /**
     * @return the batchJobDAO
     */
    public BatchJobDAO getBatchJobDAO() {
        return batchJobDAO;
    }

    /**
     * @param batchJobDAO the batchJobDAO to set
     */
    public void setBatchJobDAO(BatchJobDAO batchJobDAO) {
        this.batchJobDAO = batchJobDAO;
    }

    /**
     * @return the neutralRecordMongoAccess
     */
    public NeutralRecordAccess getNeutralRecordMongoAccess() {
        return neutralRecordMongoAccess;
    }

    /**
     * @param neutralRecordMongoAccess the neutralRecordMongoAccess to set
     */
    public void setNeutralRecordMongoAccess(NeutralRecordAccess neutralRecordMongoAccess) {
        this.neutralRecordMongoAccess = neutralRecordMongoAccess;
    }


}
