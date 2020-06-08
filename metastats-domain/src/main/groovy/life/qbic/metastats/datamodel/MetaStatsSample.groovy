package life.qbic.metastats.datamodel

class MetaStatsSample {

    final String type
    final String code
    List<String> relatives = []
    Map<String,String> properties

    /*MetaStatsSample(String code, String type, List<MetaStatsSample> samples, Map<String,String> properties){
        this.code = code
        this.type = type

        if(type == "Q_TEST_SAMPLE"){
            relatives = samples
        }
        this.properties = properties
    }
    */

    MetaStatsSample(String code, String type, Map<String,String> properties){
        this.code = code
        this.type = type
        this.properties = properties
    }

    def addRelatives(String relative){
        relatives.add(relative)
    }

}
