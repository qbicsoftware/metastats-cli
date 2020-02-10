package life.qbic.metastats.datamodel

class MetaStatsSample {

    String type
    String code
    List<MetaStatsSample> relatives = null //only collect relatives for preparation sample
    Map<String,String> properties
    //List<String> preparationSample = []

    MetaStatsSample(String code, String type, List<MetaStatsSample> samples, Map<String,String> properties){
        this.code = code
        this.type = type
        relatives = samples
        this.properties = properties
    }

    MetaStatsSample(String code, String type, Map<String,String> properties){
        this.code = code
        this.type = type
        this.properties = properties
    }

}
