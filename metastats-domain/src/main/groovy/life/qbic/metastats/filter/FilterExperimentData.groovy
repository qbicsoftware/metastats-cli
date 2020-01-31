package life.qbic.metastats.filter

import life.qbic.metastats.datamodel.MetaStatsExperiment
import life.qbic.metastats.datamodel.MetaStatsSample

interface FilterExperimentData {

    def filterSampleMetaData(List<MetaStatsSample> projectMetadata)
    def filterExperimentMetaData(List<MetaStatsExperiment> projectMetadata)

}