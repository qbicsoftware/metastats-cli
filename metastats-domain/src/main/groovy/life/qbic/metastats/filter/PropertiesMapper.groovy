package life.qbic.metastats.filter

import life.qbic.metastats.datamodel.MetaStatsExperiment
import life.qbic.metastats.datamodel.MetaStatsSample

interface PropertiesMapper {

    Map<String,String> mapExperimentProperties(Map<String,String> openBisProperties, List<MetaStatsSample> samples)
    Map<String,String> mapSampleProperties(Map<String,String> openBisProperties)
}