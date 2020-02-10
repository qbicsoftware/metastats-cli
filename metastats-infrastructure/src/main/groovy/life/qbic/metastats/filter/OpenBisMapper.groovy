package life.qbic.metastats.filter

import groovy.json.JsonSlurper

class OpenBisMapper implements PropertiesMapper{

    //todo conditions, filename, sequencing device

    Map<String,String> mapExperimentProperties(Map<String,String> openBisProperties){
        Map<String,String> metaStatsProperties = new HashMap<>()
        //Q_PROPERTIES

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

        openBisProperties.each { type, prop ->
            if (type == "Q_SECONDARY_NAME") {
                metaStatsProperties.put("sampleName", prop)
            }
        }
        return metaStatsProperties
    }

    Map<String,String> mapExpDesignProperties(Map<String,String> openBisProperties){
        "Q_SECONDARY_NAME"
        return null
    }

    Map<String,String> mapMeasurementProperties(Map<String,String> openBisProperties){
        "Q_SEQUENCER_DEVICE"
        //optional: Q_SEQUENCING_MODE, Q_SEQUENCING_TYPE
        return null
    }

    Map<String,String> mapProjectDetails(Map<String,String> openBisProperties){
        "Q_EXPERIMENTAL_SETUP"
        return null

    }


}
