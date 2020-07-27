package life.qbic.metastats.request

import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

/**
 * This use case handles how data flows into the tool
 *
 * The class requests the data for a given project code. This data is then forwarded to the output interface which transfers
 * the data to the next use case by using {@link RequestExperimentDataOutput}
 *
 * @since: 1.0
 * @author: Jennifer Bödker
 *
 */
class RequestExperimentData implements RequestExperimentDataInput {

    DatabaseGateway search
    RequestExperimentDataOutput output

    private static final Logger LOG = LogManager.getLogger(RequestExperimentData.class);

    /**
     * Creates the Request use case which obtains the database connection and a class to delegate the fetched data to
     * @param search is the database for querying the project
     * @param output class implementing the interface to delegate data out of the use case
     */
    RequestExperimentData(DatabaseGateway search, RequestExperimentDataOutput output) {
        this.search = search
        this.output = output
    }

    @Override
    void requestProjectMetadata(String projectCode) {

        if (!verifyQbicCode(projectCode)) {
            LOG.error "The project code was not valid!"
            return
        }
        LOG.info "searching for the openbis project ..."
        search.getProject(projectCode)

        LOG.info "retrieved openbis project"
        output.metaDataForProject(search.fetchSamplesWithMetadata(), search.fetchExperimentsWithMetadata())
        search.logout()
    }

    /**
     * verifies the QBiC code base on a regex
     * @param code
     * @return boolean indicating if code could be verified
     */
    static boolean verifyQbicCode(String code) {
        code ==~ "Q[A-X0-9]{4}"
    }

}
