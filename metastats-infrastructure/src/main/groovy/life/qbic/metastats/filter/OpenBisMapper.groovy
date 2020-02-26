package life.qbic.metastats.filter

import groovy.json.JsonSlurper
import life.qbic.metastats.datamodel.MetaStatsSample

class OpenBisMapper implements PropertiesMapper{

    //todo conditions, filename, sequencing device

    Map<String,String> mapExperimentProperties(Map<String,String> openBisProperties, List<MetaStatsSample> samples){
        Map<String,String> metaStatsProperties = new HashMap<>()

        ConditionParser parser = new ConditionParser()
        parser.parseProperties(openBisProperties)
        println samples

        samples.each {prepSample ->
            //check all children of prep sample to find the samples condition
            //add field "condition:$label" : "$value" -> add to samples properties
            prepSample.relatives.each {relative ->
                def res = parser.getSampleConditions(relative.code)
                if (res != null){
                    res.each {sampleProp ->
                        String value = sampleProp.value
                        String label = sampleProp.label
                        prepSample.properties.put("condition:"+label,value)
                    }
                }
            }
        }
        println "parsed xml strings"
        println samples

        return metaStatsProperties
    }

    Map<String,String> mapEntityProperties(Map<String,String> openBisProperties){
        Map<String,String> metaStatsProperties = new HashMap<>()

        openBisProperties.each { type, prop ->
            if (type == "Q_NCBI_ORGANISM") {
                metaStatsProperties.put("species", prop)
            }
            if (type == "SEX") {
                metaStatsProperties.put("sex", prop)
            }
        }
        return metaStatsProperties
    }

    Map<String,String> mapBioSampleProperties(Map<String,String> openBisProperties){
        Map<String,String> metaStatsProperties = new HashMap<>()

        openBisProperties.each { type, prop ->
            if (type == "Q_PRIMARY_TISSUE") {
                metaStatsProperties.put("tissue", prop)
            }
        }
        return metaStatsProperties
    }

    Map<String,String> mapTestSampleProperties(Map<String,String> openBisProperties){
        Map<String,String> metaStatsProperties = new HashMap<>()

        openBisProperties.each {type, prop ->
            if(type == "Q_SAMPLE_TYPE"){
                metaStatsProperties.put("analyte",prop)
            }
            if(type == "Q_SECONDARY_NAME"){
                metaStatsProperties.put("sequencingFacilityId",prop)
            }
            if(type == "Q_RIN"){
                metaStatsProperties.put("integrityNumber",prop)
            }
        }
        return metaStatsProperties
    }

    Map<String,String> mapRunProperties(Map<String,String> openBisProperties){
        Map<String,String> metaStatsProperties = new HashMap<>()

        //openBisProperties.get("Q_SECONDARY_NAME")

        openBisProperties.each { type, prop ->
            if (type == "Q_SECONDARY_NAME") {
                metaStatsProperties.put("sampleName", prop)
            }
        }
        return metaStatsProperties
    }

    Map<String,String> mapMeasurementProperties(Map<String,String> openBisProperties){
        HashMap<String,String> meta = new HashMap<>()

        meta.put("sequencingDevice",openBisProperties.get("Q_SEQUENCER_DEVICE"))
        //optional: Q_SEQUENCING_MODE, Q_SEQUENCING_TYPE
        return null
    }

    //not used for first version! TODO implement the required fields that are needed

    Map<String,String> mapExpDesignProperties(Map<String,String> openBisProperties){
        //currently no property is needed from here
        return null
    }

    Map<String,String> mapProjectDetails(Map<String,String> openBisProperties){
        //currently no property is needed from here
        return null

    }

    Map<String,String> mapSampleExtraction(Map<String,String> openBisProperties){
        //currently no property is needed from here
        return null
    }

    Map<String,String> mapSamplePrep(Map<String,String> openBisProperties){
        //currently no property is needed from here
        return null
    }

}
