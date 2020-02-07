package life.qbic.metastats.filter

import life.qbic.metastats.datamodel.MetaStatsExperiment
import life.qbic.metastats.datamodel.MetaStatsSample

interface FilterExperimentData {

    def filterProjectMetaData(List<MetaStatsSample> samples, List<MetaStatsExperiment> experiments)
}