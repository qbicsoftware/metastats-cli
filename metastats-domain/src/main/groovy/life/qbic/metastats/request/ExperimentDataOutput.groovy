package life.qbic.metastats.request

import life.qbic.metastats.datamodel.MetaStatsExperiment
import life.qbic.metastats.datamodel.MetaStatsSample

interface ExperimentDataOutput {

    def metaDataForSamples(List<MetaStatsSample> sampleMetadata)
    def metaDataForExperiment(List<MetaStatsExperiment> experimentMetadata)

}