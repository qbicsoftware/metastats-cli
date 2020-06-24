package life.qbic.metastats

import life.qbic.metastats.datamodel.MetaStatsExperiment
import life.qbic.metastats.datamodel.MetaStatsSample
import life.qbic.metastats.filter.FilterExperimentData
import life.qbic.metastats.request.ExperimentDataOutput

class PrepareMetaData implements ExperimentDataOutput {

    FilterExperimentData filterExperimentDataInput

    /**
     * Guides how data given from ExperimentDataOutput is transferred towards the use case FilterExperiment data
     * @param filterDataInput
     */
    PrepareMetaData(FilterExperimentData filterDataInput) {
        filterExperimentDataInput = filterDataInput
    }


    @Override
    def metaDataForProject(List<MetaStatsSample> sampleMetadata, List<MetaStatsExperiment> experimentMetadata) {
        filterExperimentDataInput.filterProjectMetaData(sampleMetadata, experimentMetadata)
    }

}
