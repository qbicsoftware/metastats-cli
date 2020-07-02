package life.qbic.metastats.filter

import life.qbic.metastats.datamodel.MetaStatsExperiment
import life.qbic.metastats.datamodel.MetaStatsPackageEntry
import life.qbic.metastats.datamodel.MetaStatsSample
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

class FilterExperimentDataImpl implements FilterExperimentData {

    MSMetadataPackageOutput output
    PropertiesMapper mapper
    SchemaValidator validator

    private static final Logger LOG = LogManager.getLogger(FilterExperimentDataImpl.class)

    /**
     * Creates the FilterExperiment use case has access to a schema validator, a metadata term mapper and an interface to
     * transfer the data out of the use case
     * @param output interface that defines how data is transferred out of the class
     * @param mapper defines how the data is translated from an external language e.g. openbis into the metastats metadata language
     * @param validator that is used to validate the created MetadataPackageEntry
     */
    FilterExperimentDataImpl(MSMetadataPackageOutput output, PropertiesMapper mapper, SchemaValidator validator) {
        this.output = output
        this.mapper = mapper
        this.validator = validator
    }

    @Override
    void filterProjectMetaData(List<MetaStatsSample> testSamples, List<MetaStatsExperiment> experiments) {

        LOG.info "filter metadata from samples ..."
        testSamples.each { prep ->
            if (prep.sampleType != "Q_TEST_SAMPLE") LOG.debug "the metastats sample is not a Q_TEST_SAMPLE but $prep.sampleType"
            //map the metadata terms first (otherwise duplicate names make problems later)
            prep.sampleProperties = mapper.mapSampleProperties(prep.sampleProperties)
        }

        LOG.info "filter metadata from experiment for samples ..."
        experiments.each { experiment ->
            //mapper.mapExperimentProperties(experiment, prepSamples)
            //map to samples
            testSamples.each { sample ->
                //only add the properties, do not overwrite!
                if (experiment.experimentType == "Q_NGS_MEASUREMENT") sample.sampleProperties << mapper.mapExperimentToSample(experiment, sample)
                if (experiment.experimentType == "Q_PROJECT_DETAILS") sample.sampleProperties << mapper.mapConditionToSample(experiment.experimentProperties, sample)

            }

        }

        createSequencingModeEntry(testSamples)

        LOG.info "create metadata package entries"
        List<MetaStatsPackageEntry> entries = createMetadataPackageEntries(testSamples)
        validateMetadataPackage(entries)

        output.createMetaStatsMetadataPackage(sortEntries(entries))
        output.downloadMetadataPackage()
    }

    /**
     * Method to sort the MetaStatsPackageEntries
     * @param samples
     * @return
     */
    static ArrayList sortEntries(List<MetaStatsPackageEntry> samples) {
        //sort Filenames
        samples.each { sample ->
            String fileName = sample.entryProperties.get("Filename")
            String sortedFiles = fileName.split(", ").sort().join(", ")
            sample.entryProperties.put("Filename", sortedFiles)

        }
        //sort order of QBiC.Codes
        ArrayList sortedSamples = samples.sort { it.preparationSampleId }

        return sortedSamples
    }

    /**
     * Creates MetaStatsPackageEntries from MetaStatsSamples
     * @param samples of type Q_TEST_SAMPLE
     * @return created MetaStatsPackageEntries
     */
    static List<MetaStatsPackageEntry> createMetadataPackageEntries(List<MetaStatsSample> samples) {
        List packageEntries = []

        samples.each { sample ->
            String sampleName = sample.sampleProperties.get("QBiC.Code")
            HashMap props = sample.sampleProperties as HashMap

            String res = props.get("IntegrityNumber")
            if (res != "") props.put("IntegrityNumber", Double.parseDouble(res))

            MetaStatsPackageEntry entry = new MetaStatsPackageEntry(sampleName, props)
            packageEntries.add(entry)
        }

        return packageEntries
    }

    /**
     * Method to calculate the sequencing mode of samples based on the filenames
     * @param samples
     * @return
     */
    def createSequencingModeEntry(List<MetaStatsSample> samples) {
        samples.each { sample ->
            String filename = sample.sampleProperties.get("Filename")

            String sequencingMode
            try {
                sequencingMode = SequencingModeCalculator.calculateSequencingMode(filename)

            } catch (IllegalFileType ift) {
                LOG.warn ift.message
                sequencingMode = ""
            }
            sample.sampleProperties.put("SequencingMode", sequencingMode)

        }
    }

    /**
     * Validates the MetaStatsPackageEntries for valid filenames and if the follow the schema
     * @param metadataPackage in form of a list
     */
    def validateMetadataPackage(List<MetaStatsPackageEntry> metadataPackage) {
        LOG.info "validate metastats-object-model-schema ..."

        metadataPackage.each { entry ->
            //validate filenaming and change format from list to string
            validFilenames(entry.entryProperties)

            //2. metadata need to follow schema
            if (!(validator.validateMetaStatsMetadataPackage(entry.entryProperties))) {
                LOG.info "Sample " + entry.preparationSampleId + " does not follow the schema"
            }
        }

    }

    /**
     * Validation of the filenames which must contain either the QBiC.Code or the SeqencingFacilityId
     * @param entryProps which contains all properties along with the filenames
     * @return validation status as boolean
     */
    static boolean validFilenames(HashMap entryProps) {
        boolean valid = false

        String filename = (String) entryProps.get("Filename")
        List<String> filenames = Arrays.asList(filename.split(","))

        if (filenames == null || filenames.empty) {
            return valid
        }

        filenames.each { file ->
            //a valid filename either contains the sample preparation qbic code
            def prepID = entryProps.get("QBiC.Code")
            //or the seqFacilityID
            def sampleName = entryProps.get("SequencingFacilityId")
            if (file.contains(prepID.toString()) || file.contains(sampleName.toString())) {
                valid = true
            } else {
                LOG.warn "File of sample $prepID does not follow the naming conventions: $file"
            }
        }
        return valid
    }

    /**
     * Fuses to maps into the first parameter map
     * @param target map which is extended
     * @param source map which is fused into the target map
     * @return the map with all values
     */
    static def fuseMaps(Map target, Map source) {
        source.each { key, value ->
            target.put(key, value)
        }
        return target
    }
}