package life.qbic.metastats.request

interface ExperimentDataOutput {

    def experimentDataForSamples(HashMap<String,String> sampleMetadata)
    def experimentDataForExperiment(HashMap<String,String> experimentMetadata)

}