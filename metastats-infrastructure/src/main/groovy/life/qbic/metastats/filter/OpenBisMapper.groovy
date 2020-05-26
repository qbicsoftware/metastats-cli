package life.qbic.metastats.filter


import life.qbic.metastats.datamodel.MetaStatsExperiment
import life.qbic.metastats.datamodel.MetaStatsSample
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

class OpenBisMapper implements PropertiesMapper {

    private Map sampleMappingProperties
    private Map experimentMappingProperties

    private static final Logger LOG = LogManager.getLogger(OpenBisMapper.class)

    OpenBisMapper(Map experimentProps, Map sampleProps){
        experimentMappingProperties = experimentProps
        sampleMappingProperties = sampleProps
    }

    Map mapExperimentToSample(MetaStatsExperiment experiment, MetaStatsSample sample) {
        Map<String, String> metaStatsProperties = new HashMap<>()

        if (experiment.type == "Q_PROJECT_DETAILS") {
            ConditionParser parser = new ConditionParser()
            parser.parseProperties(experiment.properties)

            //check all children of prep sample to find the samples condition
            //add field "condition:$label" : "$value" -> add to samples properties
            sample.relatives.each { relative ->
                def res = parser.getSampleConditions(relative)
                if (res != null) {
                    res.each { sampleProp ->
                        String value = sampleProp.value
                        String label = sampleProp.label
                        //LOG.debug sample.properties
                        metaStatsProperties.put("condition " + label + ":", value)
                    }
                } else {
                    LOG.info "no experiment conditions where found, check your openbis project"
                }
            }
        }
        else if (isSampleOfExperiment(experiment.samples, sample)) {
            experimentMappingProperties.each { openBisTerm,metastatsTerm ->
                String value = containsProperty(experiment.properties, openBisTerm as String)
                metaStatsProperties.put(metastatsTerm as String, value)
            }
        }
        return metaStatsProperties
    }

    boolean isSampleOfExperiment(List<String> experimentSamples, MetaStatsSample sample) {
        boolean contained = false
        //check if experiment is conducted for sample
        experimentSamples.each { expSampleCode ->
            //check prepSample code
            if (sample.code == expSampleCode) contained = true
            //check relatives
            sample.relatives.each { relativeCode ->
                if (relativeCode == expSampleCode) contained = true
            }
        }
        return contained
    }

    Map<String, String> mapSampleProperties(Map<String, String> openBisProperties) {
        Map<String, String> metaStatsProperties = new HashMap<>()
        //todo use mapping info from mapping json
        //ENTITY LEVEL
        sampleMappingProperties.each { openBisTerm, metastatsTerm ->
            String value = containsProperty(openBisProperties, openBisTerm as String)
            metaStatsProperties.put(metastatsTerm as String, value)
        }
        return metaStatsProperties
    }


    String containsProperty(Map openBisProperties, String openBisProperty) {
        if (openBisProperties.containsKey(openBisProperty)) {
            return openBisProperties.get(openBisProperty)
        }
        return ""
    }


}
