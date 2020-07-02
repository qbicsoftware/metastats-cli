package life.qbic.metastats.datamodel

class MetaStatsSample {

    final String sampleType
    final String sampleCode
    List<String> relatedSamples = []
    Map<String, String> sampleProperties

    /**
     * Creates a MetaStatsSample with a corresponding sampleCode, sample type and properties
     * @param sampleCode to identify the sample
     * @param type of the sample e.g Q_TEST_SAMPLE
     * @param sampleProperties describe the samples characteristics
     */
    MetaStatsSample(String sampleCode, String type, Map<String, String> sampleProperties) {
        this.sampleCode = sampleCode
        this.sampleType = type
        this.sampleProperties = sampleProperties
    }

    /**
     * Method to add related samples of the current sample
     * @param relative of the current sample specified by its sample code
     */
    def addRelatives(String relative) {
        relatedSamples.add(relative)
    }

}
