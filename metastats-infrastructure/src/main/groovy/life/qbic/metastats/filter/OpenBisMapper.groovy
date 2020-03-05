package life.qbic.metastats.filter

import ch.ethz.sis.openbis.generic.asapi.v3.dto.experiment.Experiment
import life.qbic.metastats.datamodel.MetaStatsExperiment
import life.qbic.metastats.datamodel.MetaStatsSample
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

class OpenBisMapper implements PropertiesMapper {

    //todo conditions, filename
    private static final Logger LOG = LogManager.getLogger(OpenBisMapper.class)


    def mapExperimentProperties(MetaStatsExperiment experiment, List<MetaStatsSample> samples) {
        println experiment.properties.keySet()

        LOG.info "parse experiment conditions ..."

        if (experiment.type == "Q_PROJECT_DETAILS") {
            ConditionParser parser = new ConditionParser()
            parser.parseProperties(experiment.properties)

            samples.each { prepSample ->
                //check all children of prep sample to find the samples condition
                //add field "condition:$label" : "$value" -> add to samples properties
                prepSample.relatives.each { relative ->
                    def res = parser.getSampleConditions(relative)
                    if (res != null) {
                        res.each { sampleProp ->
                            String value = sampleProp.value
                            String label = sampleProp.label
                            LOG.debug prepSample.properties
                            prepSample.properties.put("condition " + label + ":", value)
                        }
                    } else {
                        LOG.info "no experiment conditions where found, check your openbis project"
                    }
                }
            }
        }
        else{
            samples.each {sample ->
                if(isSampleOfExperiment(experiment.samples,sample)){
                    String value = containsProperty(experiment.properties, "Q_SEQUENCER_DEVICE")
                    sample.properties.put("sequencingDevice", value)
                    //optional: Q_SEQUENCING_MODE, Q_SEQUENCING_TYPE
                }
            }
        }
    }

    boolean isSampleOfExperiment(List<String> experimentSamples, MetaStatsSample sample){
        boolean contained = false
        //check if experiment is conducted for sample
        experimentSamples.each {expSampleCode ->
            //check prepsample code
            if(sample.code == expSampleCode) contained = true
            //check relatives
            sample.relatives.each {relativeCode ->
                if(relativeCode == expSampleCode) contained = true
            }
        }
        return contained
    }

    Map<String, String> mapSampleProperties(Map<String, String> openBisProperties) {
        Map<String, String> metaStatsProperties = new HashMap<>()

        //ENTITY LEVEL
        String value = containsProperty(openBisProperties, "Q_BIOLOGICAL_ENTITY_CODE")
        metaStatsProperties.put("individual", value)

        value = containsProperty(openBisProperties, "Q_NCBI_ORGANISM")
        metaStatsProperties.put("species", value)

        value = containsProperty(openBisProperties, "SEX")
        metaStatsProperties.put("sex", value)

        //BIOLOGICAL SAMPLE level
        value = containsProperty(openBisProperties, "Q_BIOLOGICAL_SAMPLE_CODE")
        metaStatsProperties.put("extractCode", value)

        value = containsProperty(openBisProperties, "Q_PRIMARY_TISSUE")
        metaStatsProperties.put("tissue", value)

        //TEST_SAMPLE level
        value = containsProperty(openBisProperties, "Q_TEST_SAMPLE_CODE")
        metaStatsProperties.put("samplePreparationId", value)

        value = containsProperty(openBisProperties, "Q_SAMPLE_TYPE")
        metaStatsProperties.put("analyte", value)

        value = containsProperty(openBisProperties, "Q_SECONDARY_NAME")//or Q_EXTERNALDB_ID in test sample level
        metaStatsProperties.put("sequencingFacilityId", value)

        value = containsProperty(openBisProperties, "Q_RNA_INTEGRITY_NUMBER")
        metaStatsProperties.put("integrityNumber", value)

        //RUN Level
        value = containsProperty(openBisProperties, "Q_SECONDARY_NAME_Q_NGS_SINGLE_SAMPLE_RUN")
        metaStatsProperties.put("sampleName", value)

        //DATASET Level
        value = containsProperty(openBisProperties, "Q_NGS_RAW_DATA")
        //todo convert list to proper output
        metaStatsProperties.put("filename", value)

        return metaStatsProperties
    }


    String containsProperty(Map openBisProperties, String openBisProperty) {

        if (openBisProperties.containsKey(openBisProperty)) {
            return openBisProperties.get(openBisProperty)
        } else {
            //LOG.warn "missing '$openBisProperty' property"
        }
        return "NA"
    }


}
