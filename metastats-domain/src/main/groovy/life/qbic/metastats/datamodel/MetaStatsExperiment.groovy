package life.qbic.metastats.datamodel

/**
 * Describes the characteristics of experiments within MetaStats
 *
 * This class is used as a data structure to represent experiments. It should be used to translate external experiment objects
 * into an object which is interpretable for MetaStats
 *
 * @since: 1.0
 * @author: Jennifer Bödker
 *
 */
class MetaStatsExperiment {

    final String experimentType
    List<String> relatedSampleCodes
    Map<String, String> experimentProperties

    /**
     * Creates a MetastatsExperiment for a specific type with its properties and related samples
     * @param experimentType openBis experiment type e.g. Q_PROJECT_DETAILS
     * @param experimentProperties of the experiment which differs between experiment types
     * @param sampleCodes of related samples
     */
    MetaStatsExperiment(String experimentType, Map<String, String> experimentProperties, List<String> sampleCodes) {
        this.experimentType = experimentType
        this.experimentProperties = experimentProperties
        this.relatedSampleCodes = sampleCodes
    }

}
