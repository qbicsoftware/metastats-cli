package life.qbic.metastats.request

import life.qbic.metastats.datamodel.MetaStatsExperiment
import life.qbic.metastats.datamodel.MetaStatsSample

/**
 * The interface handles the data transfer from a database
 *
 * This interface defines a gateway for the data of a database which needs to be injected into MetaStats. This interface
 * is used by the {@link RequestExperimentData} use case and allows it to request data from the database.
 * The gateway should be implemented if you want to load data from a database into MetaStats.
 *
 *
 * @since: 1.0
 * @author: Jennifer Bödker
 *
 */
interface DatabaseGateway {

    /**
     * Fetches the Project based on the project code
     * @param projectCode
     */
    void getProject(String projectCode)
    /**
     * Fetches the experiment with metadata for a project
     * @return list of MetaStatsExperiment
     */
    List<MetaStatsExperiment> fetchExperimentsWithMetadata()
    /**
     * Fetches all samples with metadata for a project
     * @return
     */
    List<MetaStatsSample> fetchSamplesWithMetadata()
    /**
     * Closes the session/ database connection
     */
    void logout()


}