package life.qbic.metastats.filter

import life.qbic.metastats.datamodel.MetaStatsExperiment
import life.qbic.metastats.datamodel.MetaStatsPackageEntry
import life.qbic.metastats.datamodel.MetaStatsSample
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

class FilterExperimentDataImpl implements FilterExperimentData {

    MSMetadataPackageOutput output
    PropertiesMapper mapper
    List<MetaStatsSample> prepSamples
    SchemaValidator validator

    private static final Logger LOG = LogManager.getLogger(FilterExperimentDataImpl.class)


    FilterExperimentDataImpl(MSMetadataPackageOutput output, PropertiesMapper mapper, SchemaValidator validator) {
        this.output = output
        this.mapper = mapper
        this.validator = validator
    }

    @Override
    def filterProjectMetaData(List<MetaStatsSample> samples, List<MetaStatsExperiment> experiments) {

        prepSamples = samples

        LOG.info "filter metadata from samples ..."
        samples.each { prep ->
            if (prep.type != "Q_TEST_SAMPLE") LOG.debug "the metastats sample is not a Q_TEST_SAMPLE but $prep.type"
            //map the metadata terms first (otherwise duplicate names make problems later)
            prep.properties = mapper.mapSampleProperties(prep.properties)
        }

        LOG.info "filter metadata from experiment for samples ..."
        experiments.each { experiment ->
            //mapper.mapExperimentProperties(experiment, prepSamples)
            //map to samples
            samples.each { sample ->
                //only add the properties, do not overwrite!
                sample.properties << mapper.mapExperimentToSample(experiment, sample)
            }
        }

        LOG.info "finished filtering of metadata package"
        LOG.info "create metadata package entries"


        List<MetaStatsPackageEntry> entries = createMetadataPackageEntries(samples)
        validateMetadataPackage(entries)

        output.createMetaStatsMetadataPackage(entries)
        output.downloadMetadataPackage()
    }

    static List<MetaStatsPackageEntry> createMetadataPackageEntries(List<MetaStatsSample> samples) {
        List packageEntries = []

        samples.each { sample ->
            String sampleName = sample.properties.get("samplePreparationId")

            HashMap props = sample.properties as HashMap

            MetaStatsPackageEntry entry = new MetaStatsPackageEntry(sampleName, props)
            packageEntries.add(entry)
        }

        return packageEntries
    }

    def validateMetadataPackage(List<MetaStatsPackageEntry> metadataPackage) {
        LOG.info "validate metastats-object-model-schema ..."

        metadataPackage.each { entry ->
            //validate filenaming and change format from list to string
            validFilenames(entry.properties)

            //2. metadata need to follow schema
            if (!(validator.validateMetaStatsMetadataPackage(entry.properties))) {
                LOG.info "Sample " + entry.entryId + " does not follow the schema"
            }
            //4. more files found than prepSamples?
            //todo
        }

    }

    static boolean validFilenames(HashMap entryProps) {
        boolean valid = true

        String filename = (String) entryProps.get("filename")
        List<String> filenames = Arrays.asList(filename.split(","))

        if(filenames == null || filenames.empty){
            return false
        }

        filenames.each { file ->
            //a valid filename either contains the sample preparation qbic code
            def prepID = entryProps.get("samplePreparationId")
            //or the seqFacilityID
            def sampleName = entryProps.get("sampleName")
            if (!file.contains(prepID.toString()) && !file.contains(sampleName.toString())){
                LOG.warn "File of sample $prepID does not follow the naming conventions: $file"
                valid = false
            }
        }

        return valid
    }

    static def add(Map target, Map values) {
        values.each { key, value ->
            target.put(key, value)
        }
    }
}