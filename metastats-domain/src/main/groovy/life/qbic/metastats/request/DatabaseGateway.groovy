package life.qbic.metastats.request

import life.qbic.metastats.datamodel.MetaStatsExperiment
import life.qbic.metastats.datamodel.MetaStatsSample

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