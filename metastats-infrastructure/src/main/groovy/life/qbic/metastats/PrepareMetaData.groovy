package life.qbic.metastats

import life.qbic.metastats.filter.FilterExperimentData
import life.qbic.metastats.request.ExperimentDataOutput

class PrepareMetaData implements ExperimentDataOutput{

    FilterExperimentData filterExperimentDataInput

    PrepareMetaData(FilterExperimentData filterDataInput){
        filterExperimentDataInput = filterDataInput
    }

    @Override
    def metaDataForSamples(HashMap<String, String> sampleMetadata) {
        filterExperimentDataInput.filterSampleMetaData(sampleMetadata)
    }

    @Override
    def metaDataForExperiment(HashMap<String, String> experimentMetadata) {
        filterExperimentDataInput.filterExperimentMetaData(experimentMetadata)
    }
}
