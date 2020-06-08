package life.qbic.metastats.datamodel

class MetaStatsExperiment {

    final String type
    List<String> samples
    Map<String,String> properties

    MetaStatsExperiment(String type, Map<String,String> properties, List<String> sampleCodes){
        this.type = type
        this.properties = properties
        this.samples = sampleCodes
    }

}
