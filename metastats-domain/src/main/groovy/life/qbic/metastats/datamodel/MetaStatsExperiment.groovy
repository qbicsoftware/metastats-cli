package life.qbic.metastats.datamodel

class MetaStatsExperiment {

    String type
    List<String> samples
    Map<String,String> properties

    MetaStatsExperiment(String type, List<String> samples, Map<String,String> properties){
        this.type = type
        this.samples = samples
        this.properties = properties
    }

}
