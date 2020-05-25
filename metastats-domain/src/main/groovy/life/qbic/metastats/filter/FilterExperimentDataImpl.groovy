package life.qbic.metastats.filter

import life.qbic.metastats.datamodel.MetaStatsExperiment
import life.qbic.metastats.datamodel.MetaStatsSample
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

class FilterExperimentDataImpl implements FilterExperimentData{

    MSMetadataPackageOutput output
    PropertiesMapper mapper
    List<MetaStatsSample> prepSamples
    SchemaValidator validator

    private static final Logger LOG = LogManager.getLogger(FilterExperimentDataImpl.class);


    FilterExperimentDataImpl(MSMetadataPackageOutput output, PropertiesMapper mapper, SchemaValidator validator){
        this.output = output
        this.mapper = mapper
        this.validator = validator
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
            //mapper.mapExperimentProperties(experiment, prepSamples)
            //map to samples
            samples.each {sample ->
                //only add the properties, do not overwrite!
                sample.properties << mapper.mapExperimentToSample(experiment,sample)
            }
        }

        LOG.info "finished filtering of metadata package"


        validateSchema(samples)

        /*prepSamples.each {
            output.createMetaStatsMetadataPackage(it.properties)
            LOG.debug it.properties
        }*/

        return null
    }


    def validateSchema(List<MetaStatsSample> samples){
        LOG.info "validate metastats-object-model-schema ..."

        samples.each {sample ->
            //1. are filenames valid
            validFilenames(sample)
            //2. metadata need to follow schema
            if(!validator.validate(sample.properties)){
               LOG.info "Sample "+ sample.code +" does not follow the schema"
            }
            //4. more files found than prepSamples?
        }

    }

    def validFilenames(MetaStatsSample sample){
        boolean valid = true
        List<String> fileNames = sample.properties.get("fileName")

        fileNames.each {file ->
            //a valid filename either contains the sample preparation qbic code
            def prepID = sample.properties.get("samplePreparationId")
            //or the seqFacilityID
            def seqFacility = sample.properties.get("sequencingFacilityId")
            if(!file.contains(prepID) && !file.contains(seqFacility)) valid = false
        }

        return valid
    }

    static def add(Map target, Map values){
        values.each {key, value ->
            target.put(key,value)
        }
    }
}