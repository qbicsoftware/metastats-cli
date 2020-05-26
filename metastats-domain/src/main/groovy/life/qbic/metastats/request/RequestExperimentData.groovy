package life.qbic.metastats.request

import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

class RequestExperimentData implements ProjectSpecification{

    DatabaseGateway search
    ExperimentDataOutput output

    private static final Logger LOG = LogManager.getLogger(RequestExperimentData.class);


    RequestExperimentData(DatabaseGateway search, ExperimentDataOutput output){
        this.search = search
        this.output = output
    }

    @Override
    def requestProjectMetadata(String projectCode) {

        if(!verifyQbicCode(projectCode)){
            LOG.error "The project code was not valid!"
            return
        }

        LOG.info "searching for the openbis project ..."
        search.getProject(projectCode)

        LOG.info "retrieved openbis project"
        output.metaDataForProject(search.fetchSamplesWithMetadata(), search.fetchExperimentsWithMetadata())
        search.logout()
    }

    static boolean verifyQbicCode(String code){
        code ==~"Q[A-X0-9]{4}"
    }

}