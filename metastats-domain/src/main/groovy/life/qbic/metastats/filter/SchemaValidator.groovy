package life.qbic.metastats.filter

import groovy.json.JsonSlurper

class SchemaValidator {

    List required
    List metaStatsFields

    //TODO use model schema from https://github.com/qbicsoftware/metastats-object-model/blob/master/model.schema.json
    Map schema

    SchemaValidator(Map schema){
        this.schema = schema
    }


    def getPropertiesFromSchema(){
        metaStatsFields = schema.get("properties").keySet() as String[]
        required = schema.get("required") as String[]
    }

    def validate(Map valueMap){
        //check if all required fields are contained
        //check if only defined terms are used
    }


}
