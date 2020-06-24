package life.qbic.metastats.request

import life.qbic.metastats.datamodel.MetaStatsExperiment
import life.qbic.metastats.datamodel.MetaStatsSample

interface ExperimentDataOutput {

    /**
     * Accepts the metadata of the project based on samples and experiment information
     * @param sampleMetadata
     * @param experimentMetadata
     * @return
     */
    def metaDataForProject(List<MetaStatsSample> sampleMetadata, List<MetaStatsExperiment> experimentMetadata)

}