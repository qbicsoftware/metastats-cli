package life.qbic.metastats.filter

import life.qbic.metastats.datamodel.MetaStatsExperiment
import life.qbic.metastats.datamodel.MetaStatsSample
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

class FilterExperimentDataImpl implements FilterExperimentData{

    MSMetadataPackageOutput output
    PropertiesMapper mapper
    List<MetaStatsSample> prepSamples

    private static final Logger LOG = LogManager.getLogger(FilterExperimentDataImpl.class);


    FilterExperimentDataImpl(MSMetadataPackageOutput output, PropertiesMapper mapper){
        this.output = output
        this.mapper = mapper
    }

    @Override
    def filterProjectMetaData(List<MetaStatsSample> samples, List<MetaStatsExperiment> experiments) {

        prepSamples = samples

        LOG.info "filter metadata from samples ..."
        samples.each { prep ->
            //map the metadata terms first (otherwise duplicate names make problems later)
            LOG.debug prep.properties
            mapper.mapSampleProperties(prep.properties)

            //todo add mapping value to openbismapper
            //todo add sample codes to openbisparser and in mapper

            //organize data so that one preparation sample has assigned all the meta data
        }

        LOG.info "filter metadata from experiment ..."
        experiments.each { experiment ->
           mapToMetaStatsTerms(experiment) //todo remember: samples of experiment also need to be mapped to prep samples
        }
        return null
    }

    /*
    Map mapToMetaStatsTerms(MetaStatsSample sample){

        return mapper.mapSampleProperties(sample.properties)

        //todo add dataset information
        if (sample.type == "Q_DATA_SET"){ //??? what is the property type?
           //todo add XXX "filename"
            //multiple files per sample are possible
        }
    }
    */

    def mapToMetaStatsTerms(MetaStatsExperiment experiment) {

        Map<String,String> meta = new HashMap<>()

        if (experiment.type == "Q_PROJECT_INFO"){
            add(meta, mapper.mapExperimentProperties(experiment.properties, prepSamples))
        }else{
            LOG.warn "missing information about experiment conditions!"
        }

        return meta
    }


    def validateSchema(){
        LOG.info "validate metastats-object-model-schema ..."
    }

    static def add(Map target, Map values){
        values.each {key, value ->
            target.put(key,value)
        }
    }
}