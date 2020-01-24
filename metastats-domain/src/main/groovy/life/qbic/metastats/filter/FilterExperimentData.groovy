package life.qbic.metastats.filter

interface FilterExperimentData {

    def filterSampleMetaData(HashMap<String,String> projectMetadata)
    def filterExperimentMetaData(HashMap<String,String> projectMetadata)

}