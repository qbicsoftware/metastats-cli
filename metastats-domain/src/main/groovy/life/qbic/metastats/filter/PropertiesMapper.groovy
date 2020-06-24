package life.qbic.metastats.filter


import life.qbic.metastats.datamodel.MetaStatsExperiment
import life.qbic.metastats.datamodel.MetaStatsSample

interface PropertiesMapper {

    /**
     * Maps the metadata terms from the experiment level onto the sample level and assigns experiment data
     * to the correct preparation sample
     * @param experiment with metadata terms
     * @param sample obtaining the experiment information
     * @return
     */
    Map mapExperimentToSample(MetaStatsExperiment experiment, MetaStatsSample sample)
    /**
     * Experimental conditions get assigned to a MetaStatsSample
     * @param experimentConditions contains the experiment conditions encoded in XML
     * @param sample gets corresponding condition info
     * @return
     */
    Map mapConditionToSample(Map experimentConditions, MetaStatsSample sample)
    /**
     * Maps the openBis properties of a sample into MetaStats terms
     * @param openBisProperties
     * @return
     */
    Map<String, String> mapSampleProperties(Map<String, String> openBisProperties)
}