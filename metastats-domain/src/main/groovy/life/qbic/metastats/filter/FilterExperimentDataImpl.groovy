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


    FilterExperimentDataImpl(MSMetadataPackageOutput output, PropertiesMapper mapper, SchemaValidator validator) {
        this.output = output
        this.mapper = mapper
        this.validator = validator
    }

    @Override
    def filterProjectMetaData(List<MetaStatsSample> samples, List<MetaStatsExperiment> experiments) {

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
            println experiment.type
            samples.each { sample ->
                //only add the properties, do not overwrite!
                if(experiment.type == "Q_NGS_MEASUREMENT") sample.properties << mapper.mapExperimentToSample(experiment, sample)
                if(experiment.type == "Q_PROJECT_DETAILS") sample.properties << mapper.mapConditionToSample(experiment.properties,sample)
            }

        }

        createSequencingModeEntry(samples)

        LOG.info "create metadata package entries"
        List<MetaStatsPackageEntry> entries = createMetadataPackageEntries(samples)
        validateMetadataPackage(entries)

        output.createMetaStatsMetadataPackage(sortEntries(entries))
        output.downloadMetadataPackage()
    }

    static ArrayList sortEntries(List<MetaStatsPackageEntry> samples){
        //sort Filenames
        samples.each {sample ->
            String fileName = sample.properties.get("Filename")
            String sortedFiles = fileName.split(", ").sort().join(", ")
            sample.properties.put("Filename",sortedFiles)
        }
        //sort order of QBiC.Codes
        ArrayList sortedSamples = samples.sort{it.entryId}

        return sortedSamples
    }

    static List<MetaStatsPackageEntry> createMetadataPackageEntries(List<MetaStatsSample> samples) {
        List packageEntries = []

        samples.each { sample ->
            String sampleName = sample.properties.get("QBiC.Code")
            HashMap props = sample.properties as HashMap

            String res = props.get("IntegrityNumber")
            if(res != "") props.put("IntegrityNumber",Double.parseDouble(res))

            MetaStatsPackageEntry entry = new MetaStatsPackageEntry(sampleName, props)
            packageEntries.add(entry)
        }

        return packageEntries
    }

    def createSequencingModeEntry(List<MetaStatsSample> samples){
        samples.each {sample ->
            String filename = sample.properties.get("Filename")
            String sequencingMode
            try{
                sequencingMode = SequencingModeCalculator.calculateSequencingMode(filename)

            }catch(IllegalFileType ift){
                LOG.warn ift.message
                sequencingMode = ""
            }
            sample.properties.put("SequencingMode",sequencingMode)
        }
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
            }else{
                LOG.warn "File of sample $prepID does not follow the naming conventions: $file"
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