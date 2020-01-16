package life.qbic.metastats.request

class RequestExperimentData implements ProjectSpecification{

    DatabaseGateway search
    ExperimentDataOutput output

    RequestExperimentData(DatabaseGateway search, ExperimentDataOutput output){
        this.search = search
        this.output = output
    }

    @Override
    def requestProjectMetadata(String projectCode) {
        search.setProject(projectCode)

        def sampleMetadata = search.getSampleMetadata()
        def experimentMetadata = search.getExperimentMetadata()

        output.experimentDataForExperiment()
        output.experimentDataForSamples()
    }

}