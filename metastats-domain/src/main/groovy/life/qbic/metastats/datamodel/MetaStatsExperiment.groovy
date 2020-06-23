package life.qbic.metastats.datamodel

class MetaStatsExperiment {

    final String experimentType
    List<String> relatedSampleCodes
    Map<String, String> properties

    /**
     * Creates a MetastatsExperiment for a specific type with its properties and related samples
     * @param experimentType openBis experiment type e.g. Q_PROJECT_DETAILS
     * @param properties of the experiment which differs between experiment types
     * @param sampleCodes of related samples
     */
    MetaStatsExperiment(String experimentType, Map<String, String> properties, List<String> sampleCodes) {
        this.experimentType = experimentType
        this.properties = properties
        this.relatedSampleCodes = sampleCodes
    }

}
