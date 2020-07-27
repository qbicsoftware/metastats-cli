package life.qbic.metastats

import life.qbic.metastats.datamodel.MetaStatsExperiment
import life.qbic.metastats.datamodel.MetaStatsSample
import life.qbic.metastats.filter.FilterExperimentDataInput
import life.qbic.metastats.request.RequestExperimentDataOutput

/**
 * Controls how data is transferred between use cases
 *
 * This class acts as a connector for the use cases RequestExperimentData and FilterExperimentData. It transfers the
 * data loaded in the RequestExperimentData use case towards the FilterExperimentData use case and allows a logical separation
 * of the use cases which makes it easier to exchange or reuse them
 *
 * @since: 1.0
 * @author: Jennifer Bödker
 *
 */
class PrepareMetaDataConnector implements RequestExperimentDataOutput {

    FilterExperimentDataInput filterExperimentDataInput

    /**
     * Guides how data given from ExperimentDataOutput is transferred towards the use case FilterExperiment data
     * @param filterDataInput specifies how experiment data is filtered
     */
    PrepareMetaDataConnector(FilterExperimentDataInput filterDataInput) {
        filterExperimentDataInput = filterDataInput
    }


    @Override
    void metaDataForProject(List<MetaStatsSample> sampleMetadata, List<MetaStatsExperiment> experimentMetadata) {
        filterExperimentDataInput.getProjectMetaData(sampleMetadata, experimentMetadata)
        filterExperimentDataInput.filter()
    }

}
