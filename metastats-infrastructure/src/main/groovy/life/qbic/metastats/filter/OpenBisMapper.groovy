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

    /**
     * Creates an OpenBisMapper based on the given maps for experiment and sample properties
     * @param experimentProps containing the corresponding metastast terms for openbis experiment properties
     * @param sampleProps containing the corresponding metastast terms for openbis sample properties
     */
    OpenBisMapper(Map experimentProps, Map sampleProps) {
        experimentMappingProperties = experimentProps
        sampleMappingProperties = sampleProps
    }

    /**
     * Maps experiment to sample properties, if the experiment contains information about that sample
     * @param experiment with database metadata information about samples
     * @param sample with properties
     * @return experimental properties based on metastats terms for the given sample
     */
    Map mapExperimentToSample(MetaStatsExperiment experiment, MetaStatsSample sample) {
        Map metaStatsProperties = new HashMap<>()
        if (isSampleOfExperiment(experiment.relatedSampleCodes, sample)) {
            experimentMappingProperties.each { openBisTerm, metastatsTerm ->
                String value = containsProperty(experiment.experimentProperties, openBisTerm as String)
                metaStatsProperties.put(metastatsTerm as String, value)
            }
        }

        return metaStatsProperties
    }

    /**
     * Maps the experimental conditions onto the preparation sample if one of its relatives or the sample itself has
     * experimental conditions
     * @param experimentConditions based on XML encoding
     * @param sample for which experimental conditions are searched
     * @return condition in form of properties for the given sample
     */
    Map mapConditionToSample(Map experimentConditions, MetaStatsSample sample) {
        HashMap metaStatsProperties = new HashMap<>()

        parser = new ConditionParser()
        parser.parseProperties(experimentConditions)

        //check all children of prep sample to find the samples condition
        sample.relatedSamples.each { relative ->
            assignSampleConditions(metaStatsProperties, relative)
        }

        //also read conditions on level of preparation sample/current sample
        assignSampleConditions(metaStatsProperties, sample.sampleCode)

        return metaStatsProperties
    }

    /**
     * Assigns the sample conditions the a sample based on its code
     * @param metaStatsProperties to which the conditions are written
     * @param sampleCode of the sample for which the conditions should be found
     */
    void assignSampleConditions(HashMap metaStatsProperties, String sampleCode) {
        def res = parser.getSampleConditions(sampleCode)

        if (res != null) {
            res.each { sampleProp ->
                String value = sampleProp.value
                String label = sampleProp.label

                if (metaStatsProperties.containsKey("Condition")) {
                    List conditions = metaStatsProperties.get("Condition") as List

                    conditions.add(new Condition(label, value))
                    //overwrites condition if already contained
                    metaStatsProperties.put("Condition", conditions)
                } else {
                    metaStatsProperties.put("Condition", [new Condition(label, value)])
                }
            }
        } else {
            LOG.info "no experiment conditions where found, check your openbis project"
        }
    }

    /**
     * Determines if a sample is part of an experiment based on list of samples assigned to an experiment
     * @param experimentSamples are samples of an experiment as codes
     * @param sample with properties with related samples
     * @return boolean to state if sample is contained in experiment
     */
    boolean isSampleOfExperiment(List<String> experimentSamples, MetaStatsSample sample) {
        boolean contained = false
        //check if experiment is conducted for sample
        experimentSamples.each { expSampleCode ->
            //check prepSample code
            if (sample.sampleCode == expSampleCode) contained = true
            //check relatives
            sample.relatedSamples.each { relativeCode ->
                if (relativeCode == expSampleCode) contained = true
            }
        }
        return contained
    }

    /**
     * Maps sample properties to metastats properties
     * @param openBisProperties , properties described with openbis terms
     * @return map of properties now described with metastats terms
     */
    Map<String, String> mapSampleProperties(Map<String, String> openBisProperties) {
        Map<String, String> metaStatsProperties = new HashMap<>()
        sampleMappingProperties.each { openBisTerm, metastatsTerm ->
            String value = containsProperty(openBisProperties, openBisTerm as String)
            metaStatsProperties.put(metastatsTerm as String, value)
        }
        return metaStatsProperties
    }

    /**
     * Checks if a given openbis property is contained in the map of properties for which a translation into
     * a metastats term exists
     * @param openBisProperties as a collection of terms that can be translated into openbis
     * @param openBisProperty as a property to define the property which is looked up
     * @return
     */
    static String containsProperty(Map openBisProperties, String openBisProperty) {
        if (openBisProperties.containsKey(openBisProperty)) {
            return openBisProperties.get(openBisProperty)
        }
        return ""
    }

}
