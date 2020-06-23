package life.qbic.metastats.filter

import life.qbic.metastats.datamodel.MetaStatsExperiment
import life.qbic.metastats.datamodel.MetaStatsSample

interface FilterExperimentData {

    /**
     * Method to filter information required for the MetaStats output from samples and experiments and transfering it into
     * a MetaStatsPackageEntry object
     *
     * @param testSamples describing Q_TEST_SAMPLES of the project with all information of related samples
     * @param experiments that are part of the project
     */
    void filterProjectMetaData(List<MetaStatsSample> testSamples, List<MetaStatsExperiment> experiments)

}