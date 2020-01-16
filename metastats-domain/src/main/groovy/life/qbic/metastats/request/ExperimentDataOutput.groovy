package life.qbic.metastats.request

interface ExperimentDataOutput {

    void experimentDataForSamples(HashMap<String,String> sampleMetadata)
    void experimentDataForExperiment(HashMap<String,String> experimentMetadata)

}