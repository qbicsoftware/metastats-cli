package life.qbic.metastats.filter

import groovy.json.JsonSlurper

class SchemaValidator {

    List required = []
    List<HashMap<String,String>> metaStatsMetadata = []

    //TODO use model schema from https://github.com/qbicsoftware/metastats-object-model/blob/master/model.schema.json
    Map schema

    SchemaValidator(Map schema){
        this.schema = schema
    }


    def getPropertiesFromSchema(){

        String[] metaStatsFields = schema.get("properties").keySet() as String[]
        String[] required = schema.get("required") as String[]

        Map properties = new HashMap<>()

        return properties
    }
}
