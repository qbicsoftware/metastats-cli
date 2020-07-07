package life.qbic.metastats.datamodel

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
