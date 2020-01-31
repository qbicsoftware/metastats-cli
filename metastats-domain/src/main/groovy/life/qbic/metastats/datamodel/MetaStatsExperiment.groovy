package life.qbic.metastats.datamodel

class MetaStatsExperiment {

    String type
    List<String> samples
    Map<String,String> properties
    String preparationSample

    MetaStatsExperiment(String type, List<String> samples, Map<String,String> properties){
        this.type = type
        this.samples = samples
        this.properties = properties
    }

}
