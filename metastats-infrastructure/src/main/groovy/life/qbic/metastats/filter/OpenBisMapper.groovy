package life.qbic.metastats.filter

import life.qbic.metastats.datamodel.Condition
import life.qbic.metastats.datamodel.MetaStatsExperiment
import life.qbic.metastats.datamodel.MetaStatsSample
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

class OpenBisMapper implements PropertiesMapper {

    private Map sampleMappingProperties
    private Map experimentMappingProperties
    private ConditionParser parser

    private static final Logger LOG = LogManager.getLogger(OpenBisMapper.class)

    OpenBisMapper(Map experimentProps, Map sampleProps) {
        experimentMappingProperties = experimentProps
        sampleMappingProperties = sampleProps
    }

    Map mapExperimentToSample(MetaStatsExperiment experiment, MetaStatsSample sample) {
        Map metaStatsProperties = new HashMap<>()
        if (isSampleOfExperiment(experiment.samples, sample)) {
            experimentMappingProperties.each { openBisTerm, metastatsTerm ->
                String value = containsProperty(experiment.properties, openBisTerm as String)
                metaStatsProperties.put(metastatsTerm as String, value)
            }
        }
        return metaStatsProperties
    }

    Map mapConditionToSample(Map experimentConditions, MetaStatsSample sample) {
        HashMap metaStatsProperties = new HashMap<>()

        parser = new ConditionParser()
        parser.parseProperties(experimentConditions)

        //check all children of prep sample to find the samples condition
        sample.relatives.each { relative ->
            assignSampleConditions(sample, metaStatsProperties, relative)
        }

        //also read conditions on level of preparation sample/current sample
        assignSampleConditions(sample, metaStatsProperties, sample.code)

        return metaStatsProperties
    }

    def assignSampleConditions(MetaStatsSample sample, HashMap metaStatsProperties, String sampleCode){
        def res = parser.getSampleConditions(sampleCode)

        if (res != null) {
            res.each { sampleProp ->
                String value = sampleProp.value
                String label = sampleProp.label

                if (metaStatsProperties.containsKey("Condition")) {
                    List conditions = metaStatsProperties.get("Condition") as List

                    conditions.add(new Condition(label, value, sample.type))
                    //overwrites condition if already contained
                    metaStatsProperties.put("Condition", conditions)
                } else {
                    metaStatsProperties.put("Condition", [new Condition(label, value, sample.type)])
                }
            }
        } else {
            LOG.info "no experiment conditions where found, check your openbis project"
        }
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

    static String containsProperty(Map openBisProperties, String openBisProperty) {
        if (openBisProperties.containsKey(openBisProperty)) {
            return openBisProperties.get(openBisProperty)
        }
        return ""
    }

}
