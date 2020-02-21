package life.qbic.metastats.filter

import life.qbic.metastats.datamodel.MetaStatsSample

interface PropertiesMapper {

    Map<String,String> mapExperimentProperties(Map<String,String> openBisProperties, List<MetaStatsSample> prepSamples)

    Map<String,String> mapEntityProperties(Map<String,String> openBisProperties)

    Map<String,String> mapBioSampleProperties(Map<String,String> openBisProperties)

    Map<String,String> mapTestSampleProperties(Map<String,String> openBisProperties)

    Map<String,String> mapRunProperties(Map<String,String> openBisProperties)

    Map<String,String> mapExpDesignProperties(Map<String,String> openBisProperties)

    Map<String,String> mapSampleExtraction(Map<String,String> openBisProperties)

    Map<String,String> mapSamplePrep(Map<String,String> openBisProperties)

    Map<String,String> mapMeasurementProperties(Map<String,String> openBisProperties)

    Map<String,String> mapProjectDetails(Map<String,String> openBisProperties)
}