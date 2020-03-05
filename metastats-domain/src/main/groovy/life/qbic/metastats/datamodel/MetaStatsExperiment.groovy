package life.qbic.metastats.datamodel

class MetaStatsExperiment {

    String type
    List<String> samples
    Map<String,String> properties

    MetaStatsExperiment(String type, Map<String,String> properties){
        this.type = type
        this.properties = properties
    }

    MetaStatsExperiment(String type, Map<String,String> properties, List samples){
        this.type = type
        this.properties = properties
        this.samples = samples
    }

    def addSamples(List<String> sampleCodes){
        this.samples = sampleCodes
    }

}
