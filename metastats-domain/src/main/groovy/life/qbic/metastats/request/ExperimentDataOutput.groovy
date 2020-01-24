package life.qbic.metastats.request

interface ExperimentDataOutput {

    def metaDataForSamples(HashMap<String,String> sampleMetadata)
    def metaDataForExperiment(HashMap<String,String> experimentMetadata)

}