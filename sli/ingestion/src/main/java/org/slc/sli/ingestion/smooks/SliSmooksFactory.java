package org.slc.sli.ingestion.smooks;

import java.io.IOException;

import java.util.List;
import java.util.Map;

import org.milyn.Smooks;
import org.milyn.delivery.Visitor;
import org.xml.sax.SAXException;

import org.slc.sli.ingestion.FileType;
import org.slc.sli.ingestion.NeutralRecord;
import org.slc.sli.ingestion.NeutralRecordFileWriter;
import org.slc.sli.ingestion.ResourceWriter;
import org.slc.sli.ingestion.dal.NeutralRecordMongoAccess;
import org.slc.sli.ingestion.dal.StagingMongoTemplate;
import org.slc.sli.ingestion.landingzone.IngestionFileEntry;
import org.slc.sli.ingestion.validation.ErrorReport;

/**
 * Factory class for Smooks
 *
 * @author dduran
 *
 */
public class SliSmooksFactory {

    private Map<FileType, SliSmooksConfig> sliSmooksConfigMap;
    private String beanId;
    private NeutralRecordMongoAccess nrMongoStagingWriter;

    public Smooks createInstance(IngestionFileEntry ingestionFileEntry, NeutralRecordFileWriter fileWriter, ErrorReport errorReport)
            throws IOException, SAXException {

        FileType fileType = ingestionFileEntry.getFileType();
        SliSmooksConfig sliSmooksConfig = sliSmooksConfigMap.get(fileType);
        if (sliSmooksConfig != null) {

            return createSmooksFromConfig(sliSmooksConfig, fileWriter, errorReport, ingestionFileEntry.getBatchJobId());

        } else {
            errorReport.fatal("File type not supported : " + fileType, SliSmooksFactory.class);
            throw new IllegalArgumentException("File type not supported : " + fileType);
        }
    }

    private Smooks createSmooksFromConfig(SliSmooksConfig sliSmooksConfig, NeutralRecordFileWriter fileWriter,
            ErrorReport errorReport, String batchJobId) throws IOException, SAXException {

        Smooks smooks = new Smooks(sliSmooksConfig.getConfigFileName());

        // based on target selectors for this file type, add visitors
        List<String> targetSelectorList = sliSmooksConfig.getTargetSelectors();
        if (targetSelectorList != null) {

            // just one visitor instance that can be added with multiple target selectors
            Visitor smooksEdFiVisitor = SmooksEdFiVisitor.createInstance(beanId, batchJobId, fileWriter, errorReport);

            nrMongoStagingWriter.getRecordRepository().setTemplate(new StagingMongoTemplate(nrMongoStagingWriter.getRecordRepository().getTemplate().getDatabasePrefix(), batchJobId, nrMongoStagingWriter.getRecordRepository().getTemplate().getNeutralRecordMappingConverter()));

            ((SmooksEdFiVisitor) smooksEdFiVisitor).setNrMongoStagingWriter(nrMongoStagingWriter);
            for (String targetSelector : targetSelectorList) {
                smooks.addVisitor(smooksEdFiVisitor, targetSelector);
            }
        }
        return smooks;
    }

    public void setSliSmooksConfigMap(Map<FileType, SliSmooksConfig> sliSmooksConfigMap) {
        this.sliSmooksConfigMap = sliSmooksConfigMap;
    }

    public void setBeanId(String beanId) {
        this.beanId = beanId;
    }

    public void setNrMongoStagingWriter(ResourceWriter<NeutralRecord> nrMongoStagingWriter) {
        this.nrMongoStagingWriter = (NeutralRecordMongoAccess) nrMongoStagingWriter;
    }
}
