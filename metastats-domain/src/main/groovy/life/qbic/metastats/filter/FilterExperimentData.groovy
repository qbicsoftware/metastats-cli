package life.qbic.metastats.filter

import life.qbic.metastats.datamodel.MetaStatsExperiment
import life.qbic.metastats.datamodel.MetaStatsPackageEntry
import life.qbic.metastats.datamodel.MetaStatsSample
import life.qbic.metastats.exceptions.InvalidFileNameException
import life.qbic.metastats.exceptions.InvalidProjectStructureException

/**
 * Use case to handle filtering of the data loaded by the RequestExperimentData use case
 *
 * This class filters experiment and sample data for the required metadata of the output. The data is verified and forwarded
 * to an output class {@link FilterExperimentDataFileOutput}
 *
 * @since: 1.0
 * @author: Jennifer Bödker
 *
 */
class FilterExperimentData implements FilterExperimentDataInput {

    private FilterExperimentDataFileOutput output
    private MessageOutputPort messageOutputPort
    private PropertiesMapper mapper
    private SchemaValidator validator

    private List<MetaStatsSample> testSamples
    private List<MetaStatsExperiment> experiments


    /**
     * Creates the FilterExperiment use case, which has access to a schema validator, a metadata term mapper and an interface to
     * transfer the data out of the use case
     * @param output interface that defines how data is transferred out of the class
     * @param mapper defines how the data is translated from an external language e.g. openbis into the metastats metadata language
     * @param validator that is used to validate the created MetadataPackageEntry
     */
    FilterExperimentData(FilterExperimentDataFileOutput output, PropertiesMapper mapper, SchemaValidator validator, MessageOutputPort messageOutputPort) {
        this.output = output
        this.messageOutputPort = messageOutputPort
        this.mapper = mapper
        this.validator = validator
    }

    @Override
    void getProjectMetaData(List<MetaStatsSample> testSamples, List<MetaStatsExperiment> experiments) {
        this.testSamples = testSamples
        this.experiments = experiments
    }

    /**
     * This method handles the logic of this use case by filtering the obtained samples and experiments for the required
     * metadata, mapping the terms and transferring it towards the output interface
     */
    void filter() {
        testSamples.each { prep ->
            if (prep.sampleType != "Q_TEST_SAMPLE") throw new InvalidProjectStructureException("The metastats sample is not a Q_TEST_SAMPLE but $prep.sampleType")
            //map the metadata terms first (otherwise duplicate names make problems later)
            prep.sampleProperties = mapper.mapSampleProperties(prep.sampleProperties)
        }

        experiments.each { experiment ->
            //map to samples
            testSamples.each { sample ->
                //only add the properties, do not overwrite!
                if (experiment.experimentType == "Q_NGS_MEASUREMENT") sample.sampleProperties << mapper.mapExperimentToSample(experiment, sample)
                if (experiment.experimentType == "Q_PROJECT_DETAILS") sample.sampleProperties << mapper.mapConditionToSample(experiment.experimentProperties, sample)
            }
        }

        createSequencingModeEntry(testSamples)

        List<MetaStatsPackageEntry> entries = createMetadataPackageEntries(testSamples)
        validateMetadataPackage(entries)

        output.write(sortEntries(entries))
    }

    /**
     * Method to sort the MetaStatsPackageEntries
     * @param samples
     * @return ArrayList of sorted MetaStatsPackageEntries
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
     * @return List of created MetaStatsPackageEntries
     */
    static List<MetaStatsPackageEntry> createMetadataPackageEntries(List<MetaStatsSample> samples) {
        List packageEntries = []

        samples.each { sample ->
            String sampleName = sample.sampleProperties.get("QBiC.Code")
            HashMap props = sample.sampleProperties as HashMap

            String res = props.get("IntegrityNumber")
            if (res != "") props.put("IntegrityNumber", Double.parseDouble(res))
            //else the number remains an empty string and is validated by the schema.

            MetaStatsPackageEntry entry = new MetaStatsPackageEntry(sampleName, props)
            packageEntries.add(entry)
        }

        return packageEntries
    }

    /**
     * Method to calculate the sequencing mode of samples based on the filenames
     * @param samples
     */
    void createSequencingModeEntry(List<MetaStatsSample> samples){
        samples.each { sample ->
            String filesPerSample = sample.sampleProperties.get("Filename")
            List<String> sampleFiles = filesPerSample.split(",")
            SequencingMode sequencingMode = SequencingModeCalculator.calculateSequencingMode(sampleFiles)

            if(sequencingMode == SequencingMode.NA){
                messageOutputPort.invokeOnError("The sequencing mode cannot be defined for $sample.sampleCode",FilterExperimentData.class)
            }

            sample.sampleProperties.put("SequencingMode", sequencingMode.seqMode)
        }
    }

    /**
     * Checks and validates whether the MetaStatsPackageEntries Objects and filenames adhere to the schema
     * @param metadataPackage list of MetaStatsPackageEntry Objects
     */
    void validateMetadataPackage(List<MetaStatsPackageEntry> metadataPackage) {

        metadataPackage.each { entry ->
            //validate filenaming and change format from list to string
            try{
                validateFilenames(entry.entryProperties)
            }catch(InvalidFileNameException invalidFileNameException){
                messageOutputPort.invokeOnError(invalidFileNameException.message, FilterExperimentData.class)
            }

            //2. metadata need to follow schema
            if (!(validator.validateMetaStatsMetadataPackage(entry.entryProperties))) {
                messageOutputPort.invokeOnError("Sample " + entry.preparationSampleId + " does not follow the schema", FilterExperimentData.class)
            }
        }

    }

    /**
     * Validation of the filenames which must contain either the QBiC.Code or the SeqencingFacilityId
     * @param entryProps which contains all properties along with the filenames
     */
    protected void validateFilenames(HashMap entryProps) {

        String filename = (String) entryProps.get("Filename")
        String sampleCode = entryProps.get("QBiC.Code") as String
        String sequencingFacilityId = entryProps.get("SequencingFacilityId") as String

        if (!filename) {
            throw new InvalidFileNameException("There are no files given for your sample with the QBiC.Code $sampleCode")
        }

        List<String> filesPerSample = Arrays.asList(filename.split(","))

        filesPerSample.each { file ->
            try{
                checkFileName(file, sampleCode, sequencingFacilityId)
            }catch(InvalidFileNameException e){
                throw new InvalidFileNameException(e.message)
            }
        }
    }

    /**
     * A valid filename either contains the sample preparation qbic code or the seqFacilityID
     * @param filename of a sequencing file of a sample
     * @param prepID of the sample based on the QBiC.Code
     * @param sequencingFacilityId of the sample based on the sample name given by the sequencing facility
     */
    private void checkFileName(String filename, String prepID, String sequencingFacilityId){
        if (!filename.contains(prepID.toString()) && !filename.contains(sequencingFacilityId.toString())) {
            throw new InvalidFileNameException("File of sample $prepID does not follow the naming conventions: $filename")
        }
    }

    /**
     * Merges two maps into singular map
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
