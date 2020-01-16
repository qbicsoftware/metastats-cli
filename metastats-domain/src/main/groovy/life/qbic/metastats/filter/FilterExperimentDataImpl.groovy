package life.qbic.metastats.filter

class FilterExperimentDataImpl implements FilterExperimentData{

    MSMetadataPackageOutput output

    FilterExperimentData(MSMetadataPackageOutput output){
        this.output = output
    }

    //todo check if MS package data is complete --> warnings

    @Override
    void filterMetadataData(HashMap<String, String> projectMetadata) {
        //data defined with json
        //output.setMetaStatsMetadataPackage(kddkdkk)
    }
}