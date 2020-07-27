package life.qbic.metastats.request

import life.qbic.metastats.datamodel.MetaStatsExperiment
import life.qbic.metastats.datamodel.MetaStatsSample

/**
 * Handles the output of the data from the RequestExperimentData use case
 *
 * This class defines what data from the use case needs to be accepted by an implementing class.
 * It allows a controlled data flow from the domain module towards the infrastructure module.
 *
 * @since: 1.0
 * @author: Jennifer Bödker
 *
 */
interface RequestExperimentDataOutput {

    /**
     * Accepts the metadata of the project based on samples and experiment information
     * @param sampleMetadata
     * @param experimentMetadata
     */
    void metaDataForProject(List<MetaStatsSample> sampleMetadata, List<MetaStatsExperiment> experimentMetadata)

}