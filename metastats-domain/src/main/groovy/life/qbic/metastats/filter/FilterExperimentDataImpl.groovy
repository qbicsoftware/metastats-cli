package life.qbic.metastats.filter

import life.qbic.metastats.datamodel.MetaStatsExperiment
import life.qbic.metastats.datamodel.MetaStatsSample

class FilterExperimentDataImpl implements FilterExperimentData{

    MSMetadataPackageOutput output
    PropertiesMapper mapper
    //Map<String,String>

    FilterExperimentDataImpl(MSMetadataPackageOutput output, PropertiesMapper mapper){
        this.output = output
        this.mapper = mapper
    }

    @Override
    def filterProjectMetaData(List<MetaStatsSample> samples, List<MetaStatsExperiment> experiments) {

        samples.each { prepSamples ->
            //map the metadata terms first (otherwise duplicate names make problems later)
            mapToMetaStatsTerms(prepSamples)
            //organize data so that one preparation sample has assigned all the meta data
        }

        experiments.each { experiment ->
           mapToMetaStatsTerms(experiment) //todo remember: samples of experiment also need to be mapped to prep samples
        }
        return null
    }

    def mapToMetaStatsTerms(MetaStatsSample sample){

        Map<String,String> meta = new HashMap<>()

        if (sample.type == "Q_TEST_SAMPLE"){
            meta.put("samplePreparationId",sample.code)
            add(meta,mapper.mapTestSampleProperties(sample.properties))
        }

        sample.relatives.each {
            if (it.type == "Q_BIOLOGICAL_ENTITY"){
                meta.put("individual",it.code)
                add(meta, mapper.mapEntityProperties(it.properties))
            }
            if (it.type == "Q_BIOLOGICAL_SAMPLE"){
                meta.put("extractCode",it.code)
                add(meta, mapper.mapBioSampleProperties(it.properties))
            }

            if (it.type =~ "^Q.+_RUN\$"){
                add(meta,mapper.mapRunProperties(it.properties))
            }
        }

        //todo add dataset to download
        /**if (sample.type == "Q_DATA_SET"){ //??? what is the property type?
           //todo add XXX "filename"
            //multiple files per sample are possible
        }*/

    }

    def mapToMetaStatsTerms(MetaStatsExperiment experiment) {
        Map<String,String> meta = new HashMap<>()

        if (experiment.type == "Q_EXPERIMENTAL_DESIGN"){
            add(meta, mapper.mapExpDesignProperties(experiment.properties))
        }
        if (experiment.type =~ "Q_[A-Z]*_MEASUREMENT"){
            add(meta, mapper.mapMeasurementProperties(experiment.properties))
        }
        if (experiment.type == "Q_PROJECT_DETAILS"){
            add(meta, mapper.mapProjectDetails(experiment.properties))
        }
    }

    static def add(Map target, Map values){
        values.each {key, value ->
            target.put(key,value)
        }
    }

    def validateSchema(){

    }

}