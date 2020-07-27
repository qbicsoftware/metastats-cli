package life.qbic.metastats.filter

import life.qbic.metastats.datamodel.MetaStatsExperiment
import life.qbic.metastats.datamodel.MetaStatsSample

/**
 * The interface defines functions required in a mapper class
 *
 * A mapper class maps the metadata from different objects into a single map. The output of MetaStats should be translated
 * into a single map from different project levels such as sample and experiment. The metadata needs to be associated with the
 * corresponding sample.
 * Furthermore the metadata terms must me translated into the MetaStats terminology
 *
 * @since: 1.0
 * @author: Jennifer Bödker
 *
 */
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
     * @return Map that associates the Sample with the corresponding experiment
     */
    Map mapConditionToSample(Map experimentConditions, MetaStatsSample sample)
    /**
     * Maps the openBis properties of a sample into MetaStats terms
     * @param openBisProperties
     * @return Map that associates the sample with the corresponding experiment conditions
     */
    Map<String, String> mapSampleProperties(Map<String, String> openBisProperties)
}
