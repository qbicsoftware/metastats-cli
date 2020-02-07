package life.qbic.metastats

import life.qbic.metastats.datamodel.MetaStatsExperiment
import life.qbic.metastats.datamodel.MetaStatsSample
import life.qbic.metastats.filter.FilterExperimentData
import life.qbic.metastats.request.ExperimentDataOutput

class PrepareMetaData implements ExperimentDataOutput{

    FilterExperimentData filterExperimentDataInput

    PrepareMetaData(FilterExperimentData filterDataInput){
        filterExperimentDataInput = filterDataInput
    }


    @Override
    def metaDataForSamples(List<MetaStatsSample> sampleMetadata) {
        filterExperimentDataInput.filterSampleMetaData(sampleMetadata)
    }

    @Override
    def metaDataForExperiment(List<MetaStatsExperiment> experimentMetadata) {
        filterExperimentDataInput.filterExperimentMetaData(experimentMetadata)
    }
}
