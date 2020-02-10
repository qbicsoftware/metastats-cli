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

        samples.each { biologicalSample ->
            //map the metadata terms first (otherwise duplicate names make problems later)
            mapToMetaStatsTerms(biologicalSample)
            //organize data so that one preparation sample has assigned all the meta data
        }

        experiments.each { experiment ->
           // mapToMetaStatsTerms(experiment)
            print "null"
        }
        return null
    }

    def mapToMetaStatsTerms(MetaStatsSample sample){

        Map<String,String> meta = new HashMap<>()

        if (sample.type == "Q_BIOLOGICAL_ENTITY"){
            meta.put("individual",sample.code)
            add(meta, mapper.mapEntityProperties(sample.properties))
        }
        if (sample.type == "Q_BIOLOGICAL_SAMPLE"){
            meta.put("extractCode",sample.code)
            add(meta, mapper.mapBioSampleProperties(sample.properties))
        }
        if (sample.type == "Q_TEST_SAMPLE"){
            meta.put("samplePreparationId",sample.code)
            add(meta,mapper.mapTestSampleProperties(sample.properties))
        }
        if (sample.type =~ "^Q.+_RUN\$"){
            add(meta,mapper.mapRunProperties(sample.properties))
        }
        //todo add dataset to download
        /**if (sample.type == "Q_DATA_SET"){ //??? what is the property type?
           //todo add XXX "filename"
            //multiple files per sample are possible
        }*/

        if (sample.relatives != null){
            sample.relatives.each { child ->
                mapToMetaStatsTerms(child)
            }
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