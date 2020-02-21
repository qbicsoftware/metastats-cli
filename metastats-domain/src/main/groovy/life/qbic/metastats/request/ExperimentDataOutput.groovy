package life.qbic.metastats.request

import life.qbic.metastats.datamodel.MetaStatsExperiment
import life.qbic.metastats.datamodel.MetaStatsSample

interface ExperimentDataOutput {

    def metaDataForProject(List<MetaStatsSample> sampleMetadata,List<MetaStatsExperiment> experimentMetadata)

}