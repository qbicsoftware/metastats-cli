package life.qbic.metastats.datamodel

class MetaStatsExperiment {

    String type
    //String prepSampleCode
    Map<String,String> properties

    MetaStatsExperiment(String type, Map<String,String> properties){
        this.type = type
        this.properties = properties
    }

    /*def addPrepSample(String sampleCode){
        this.prepSampleCode = sampleCode
    }*/

}
