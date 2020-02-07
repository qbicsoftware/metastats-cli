package life.qbic.metastats.datamodel

class MetaStatsSample {

    String type
    String code
    List<MetaStatsSample> children = null
    Map<String,String> properties
    List<String> preparationSample = []

    MetaStatsSample(String code, String type, List<MetaStatsSample> samples, Map<String,String> properties){
        this.code = code
        this.type = type
        children = samples
        this.properties = properties
    }

    MetaStatsSample(String code, String type, Map<String,String> properties){
        this.code = code
        this.type = type
        this.properties = properties
    }

}
