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

        if(!verifyQbicCode(projectCode)){
            //add log4j??
            //todo show in commandline
            println "The project code was not valid"
            return null
        }

        search.getProject(projectCode)

        output.metaDataForProject(search.getSamplesWithMetadata(), search.getExperimentsWithMetadata())

        println "successfully parsed openbis project"
    }

    static boolean verifyQbicCode(String code){
        code ==~"Q[A-X0-9]{4}"
    }

}