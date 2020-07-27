package life.qbic.metastats.filter

import life.qbic.metastats.datamodel.MetaStatsExperiment
import life.qbic.metastats.datamodel.MetaStatsSample

/**
 * The interface handles which data flows into the FilterExperimentData use case
 *
 * This interface acts as a contract to determine which data is allowed to flow into the use case
 *
 * @since: 1.0
 * @author: Jennifer Bödker
 *
 */
interface FilterExperimentDataInput {

    /**
     * Method to filter information required for the MetaStats output from samples and experiments and transfering it into
     * a MetaStatsPackageEntry object
     *
     * @param testSamples describing Q_TEST_SAMPLES of the project with all information of related samples
     * @param experiments that are part of the project
     */
    void getProjectMetaData(List<MetaStatsSample> testSamples, List<MetaStatsExperiment> experiments)
    void filter()

}