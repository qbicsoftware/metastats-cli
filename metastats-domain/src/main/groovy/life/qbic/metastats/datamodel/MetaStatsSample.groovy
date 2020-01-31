package life.qbic.metastats.datamodel

class MetaStatsSample {

    //todo delete type property, a metastats doesn't need a sample type
    String type
    String code
    //todo delete children property, a metastats sample has no children but only properties of different sample types
    List<MetaStatsSample> children
    Map<String,String> properties
    //todo delete preparationSample property, a metastats does not requiere a list of prep. samples!
    List<String> preparationSample = []

    MetaStatsSample(String code, String type, List<MetaStatsSample> samples, Map<String,String> properties){
        this.code = code
        this.type = type
        children = samples
        this.properties = properties
    }

}
