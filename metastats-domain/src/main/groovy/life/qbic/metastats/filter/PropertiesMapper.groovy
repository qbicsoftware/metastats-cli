package life.qbic.metastats.filter

interface PropertiesMapper {

    Map<String,String> mapExperimentProperties(Map<String,String> openBisProperties)

    Map<String,String> mapEntityProperties(Map<String,String> openBisProperties)

    Map<String,String> mapBioSampleProperties(Map<String,String> openBisProperties)

    Map<String,String> mapTestSampleProperties(Map<String,String> openBisProperties)

    Map<String,String> mapRunProperties(Map<String,String> openBisProperties)

   // Map<String,String> mapDataSetProperties(Map<String,String> openBisProperties)
}