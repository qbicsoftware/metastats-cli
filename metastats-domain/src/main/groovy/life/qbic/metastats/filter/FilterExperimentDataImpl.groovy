package life.qbic.metastats.filter

import life.qbic.metastats.datamodel.MetaStatsExperiment
import life.qbic.metastats.datamodel.MetaStatsSample

class FilterExperimentDataImpl implements FilterExperimentData{

    MSMetadataPackageOutput output

    FilterExperimentDataImpl(MSMetadataPackageOutput output){
        this.output = output
    }

    @Override
    def filterSampleMetaData(List<MetaStatsSample> projectMetadata) {
        //for each preparation sample get information like from schema
        return null
    }

    @Override
    def filterExperimentMetaData(List<MetaStatsExperiment> projectMetadata) {
        return null
    }


    def validateSchema(){

    }

}