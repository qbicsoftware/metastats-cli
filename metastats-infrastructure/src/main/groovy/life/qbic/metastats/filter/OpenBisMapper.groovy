package life.qbic.metastats.filter


import life.qbic.metastats.datamodel.MetaStatsSample
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

class OpenBisMapper implements PropertiesMapper{

    //todo conditions, filename
    private static final Logger LOG = LogManager.getLogger(OpenBisMapper.class)


    Map<String,String> mapExperimentProperties(Map<String,String> openBisProperties, List<MetaStatsSample> samples){
        Map<String,String> metaStatsProperties = new HashMap<>()

        if(openBisProperties.containsKey("")){
            ConditionParser parser = new ConditionParser()
            parser.parseProperties(openBisProperties)

            samples.each {prepSample ->
                //check all children of prep sample to find the samples condition
                //add field "condition:$label" : "$value" -> add to samples properties
                prepSample.relatives.each {relative ->
                    def res = parser.getSampleConditions(relative)
                    if (res != null){
                        res.each {sampleProp ->
                            String value = sampleProp.value
                            String label = sampleProp.label
                            LOG.debug prepSample.properties
                            prepSample.properties.put("condition:"+label,value)
                        }
                    }else{
                        LOG.info "no experiment conditions where found, check your openbis project"
                    }
                }
            }
        }

        //MEASUREMENT level
        String value = containsProperty(openBisProperties,"Q_SEQUENCER_DEVICE")
        metaStatsProperties.put("sequencingDevice", value)
        //optional: Q_SEQUENCING_MODE, Q_SEQUENCING_TYPE

        LOG.info "parsed experiment conditions"
        LOG.debug samples

        return metaStatsProperties
    }

    Map<String,String> mapSampleProperties(Map<String,String> openBisProperties){
        Map<String,String> metaStatsProperties = new HashMap<>()

        //ENTITY LEVEL
        String value = containsProperty(openBisProperties,"Q_BIOLOGICAL_ENTITY_CODE")
        metaStatsProperties.put("individual", value)

        value = containsProperty(openBisProperties,"Q_NCBI_ORGANISM")
        metaStatsProperties.put("species", value)

        value = containsProperty(openBisProperties,"SEX")
        metaStatsProperties.put("sex", value)

        //BIOLOGICAL SAMPLE level
        value = containsProperty(openBisProperties,"Q_BIOLOGICAL_SAMPLE_CODE")
        metaStatsProperties.put("extractCode", value)

        value = containsProperty(openBisProperties,"Q_PRIMARY_TISSUE")
        metaStatsProperties.put("tissue", value)

        //TEST_SAMPLE level
        value = containsProperty(openBisProperties,"Q_TEST_SAMPLE_CODE")
        metaStatsProperties.put("samplePreparationId", value)

        value = containsProperty(openBisProperties,"Q_SAMPLE_TYPE")
        metaStatsProperties.put("analyte", value)

        value = containsProperty(openBisProperties,"Q_SECONDARY_NAME")//or Q_EXTERNALDB_ID in test sample level
        metaStatsProperties.put("sequencingFacilityId", value)

        value = containsProperty(openBisProperties,"Q_RNA_INTEGRITY_NUMBER")
        metaStatsProperties.put("integrityNumber", value)

        //RUN Level
        value = containsProperty(openBisProperties,"Q_SECONDARY_NAME_Q_NGS_SINGLE_SAMPLE_RUN")
        metaStatsProperties.put("sampleName", value)

        //DATASET Level
        value = containsProperty(openBisProperties,"Q_NGS_RAW_DATA")
        //todo convert list to proper output
        metaStatsProperties.put("filename",value)

        return metaStatsProperties
    }


    String containsProperty(Map openBisProperties, String openBisProperty){

        if(openBisProperties.containsKey(openBisProperty)){
            return openBisProperties.get(openBisProperty)
        }else{
           //LOG.warn "missing '$openBisProperty' property"
        }
        return "NA"
    }



}
