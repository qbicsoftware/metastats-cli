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

    }

}