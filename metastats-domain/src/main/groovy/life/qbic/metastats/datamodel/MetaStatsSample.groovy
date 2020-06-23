package life.qbic.metastats.datamodel

class MetaStatsSample {

    final String sampleType
    final String sampleCode
    List<String> relatedSamples = []
    Map<String, String> properties

    /**
     * Creates a MetaStatsSample with a corresponding sampleCode, sample type and properties
     * @param sampleCode to identify the sample
     * @param type of the sample e.g Q_TEST_SAMPLE
     * @param properties describe the samples characteristics
     */
    MetaStatsSample(String sampleCode, String type, Map<String, String> properties) {
        this.sampleCode = sampleCode
        this.sampleType = type
        this.properties = properties
    }

    /**
     * Method to add related samples of the current sample
     * @param relative of the current sample specified by its sample code
     */
    def addRelatives(String relative) {
        relatedSamples.add(relative)
    }

}
