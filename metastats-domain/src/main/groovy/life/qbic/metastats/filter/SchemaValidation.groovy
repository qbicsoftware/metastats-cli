package life.qbic.metastats.filter

import groovy.json.JsonSlurper

class SchemaValidation {

    List required = []
    List<HashMap<String,String>> metaStatsMetadata = []

//TODO use model schema from https://github.com/qbicsoftware/metastats-object-model/blob/master/model.schema.json
//TODO needs CI but for now use a local copy of the model in the project
    def schema = getClass().getResource("/model.schema.json")

    def parseSchema(){
        new JsonSlurper().parseText(schema.text)
        //use it like .get("properties")
    }


    def getPropertiesFromSchema(){
        def jsonSchema = parseSchema()

        String[] metaStatsFields = jsonSchema.get("properties").keySet() as String[]

        Map properties = new HashMap<>()

        jsonSchema.each {key,value ->
            if(key == "properties"){
                properties = value
                println properties
            }
            if(key == "required"){
                required = value
                println required
            }
        }
        return properties
    }
}
