package life.qbic.metastats.filter

import life.qbic.metastats.datamodel.MetaStatsExperiment
import life.qbic.metastats.datamodel.MetaStatsSample
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

class FilterExperimentDataImpl implements FilterExperimentData{

    MSMetadataPackageOutput output
    PropertiesMapper mapper
    List<MetaStatsSample> prepSamples
    Map validSchema

    private static final Logger LOG = LogManager.getLogger(FilterExperimentDataImpl.class);


    FilterExperimentDataImpl(MSMetadataPackageOutput output, PropertiesMapper mapper, Map schema){
        this.output = output
        this.mapper = mapper
        validSchema = schema
    }

    @Override
    def filterProjectMetaData(List<MetaStatsSample> samples, List<MetaStatsExperiment> experiments) {

        prepSamples = samples

        LOG.info "filter metadata from samples ..."
        samples.each { prep ->
            if(prep.type != "Q_TEST_SAMPLE") LOG.debug "the metastats sample is not a Q_TEST_SAMPLE but $prep.type"
            //map the metadata terms first (otherwise duplicate names make problems later)
            prep.properties = mapper.mapSampleProperties(prep.properties)
        }

        LOG.info "filter metadata from experiment ..."
        experiments.each {experiment ->
            mapper.mapExperimentProperties(experiment, prepSamples)
        }

        LOG.info "finished filtering of metadata package"


        validateSchema()

        prepSamples.each {
            output.createMetaStatsMetadataPackage(it.properties)
            LOG.debug it.properties
        }

        return null
    }


    def validateSchema(){
        LOG.info "validate metastats-object-model-schema ..."
        SchemaValidator validator = new SchemaValidator(validSchema)
        //1. are filenames valid
        //2. are required schema fields included
        //3. are valid names used
        //4. more files found than prepSamples?
    }

    static def add(Map target, Map values){
        values.each {key, value ->
            target.put(key,value)
        }
    }
}