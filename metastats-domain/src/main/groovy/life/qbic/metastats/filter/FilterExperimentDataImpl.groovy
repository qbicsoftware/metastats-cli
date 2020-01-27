package life.qbic.metastats.filter

import groovy.json.JsonSlurper

//TODO use model schema from https://github.com/qbicsoftware/metastats-object-model/blob/master/model.schema.json needs CI but for now use a local copy of the model in the project
class FilterExperimentDataImpl implements FilterExperimentData{

    MSMetadataPackageOutput output
    def schema = getClass().getResource("/model.schema.json")

    FilterExperimentDataImpl(MSMetadataPackageOutput output){
        this.output = output
    }

    def parseSchema(){
        new JsonSlurper().parseText(schema.text)
        //use it like .get("properties")
    }

    @Override
    def filterSampleMetaData(HashMap<String, String> projectMetadata) {
        return null
    }

    @Override
    def filterExperimentMetaData(HashMap<String, String> projectMetadata) {
        return null
    }

    def createMetaStatsMetadataPackage(HashMap<String,String> sampleMeta, HashMap<String,String> experimentMeta){
        def jsonSchema = parseSchema()
        def propMap = jsonSchema.get("properties")

        String[] keys = propMap.keySet() as String[]
        //todo how to map from openbis fields to schema fields? add values to schema
        //reduce raw metadata to fields required for metadata package

    }

    def handelFileNames(){

    }

    def validateSchema(){

    }
}