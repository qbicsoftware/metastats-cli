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
        //set the project
        search.setProject(projectCode)

        if(!verifyQbicCode(projectCode)){
            //add log4j??
            println "The project code was not valid"
            return null
        }

        HashMap sampleMetaData = search.getSampleMetadata()
        HashMap expMetadata = search.getExperimentMetadata()

        [sampleMetaData,expMetadata]
    }

    boolean verifyQbicCode(String code){
        code ==~"Q[A-X0-9]{4}"
    }

}