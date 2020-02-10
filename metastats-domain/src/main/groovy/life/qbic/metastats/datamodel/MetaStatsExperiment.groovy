package life.qbic.metastats.datamodel

class MetaStatsExperiment {

    String type
    List<String> sampleCodes
    Map<String,String> properties

    MetaStatsExperiment(String type, List<String> sampleCodes, Map<String,String> properties){
        this.type = type
        this.sampleCodes = sampleCodes
        this.properties = properties
    }

}
