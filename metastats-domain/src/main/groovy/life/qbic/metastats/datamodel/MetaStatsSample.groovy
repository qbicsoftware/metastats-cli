package life.qbic.metastats.datamodel

class MetaStatsSample {

    String type
    String code
    List<MetaStatsSample> children
    Map<String,String> properties

    MetaStatsSample(String code, String type, List<MetaStatsSample> samples, Map<String,String> properties){
        this.type = type
        children = samples
        this.properties = properties
    }

}
