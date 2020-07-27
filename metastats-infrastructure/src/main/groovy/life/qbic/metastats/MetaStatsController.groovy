package life.qbic.metastats

import life.qbic.metastats.fileCreator.TsvFileOutputCreator
import life.qbic.metastats.filter.*
import life.qbic.metastats.io.JsonParser
import life.qbic.metastats.request.*
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

/**
 * Controls how data flows into MetaStats
 *
 * This class creates instances of MetaStats classes and injects them as described in the architectural draft.
 * It also handles how the input of the commandline flows into the classes.
 *
 * @since: 1.0
 * @author: Jennifer Bödker
 *
 */
class MetaStatsController {

    private String configFile
    private String projectCode
    private RequestExperimentDataInput spec
    private FilterExperimentDataInput filterExperimentData

    private static final Logger LOG = LogManager.getLogger(MetaStatsController.class)

    /**
     * Sets up the use cases and infrastructure based on the specified configuration file and the given project code
     * @param conf with information about the database connection
     * @param projectCode for which the data is fetched
     */
    MetaStatsController(String conf, String projectCode) {
        this.configFile = conf
        this.projectCode = projectCode
    }

    /**
     * Starts the RequestExperimentData use case
     * @param schemaPath of metadata schema for validation
     * @param sampleSchema to define valid samples descriptions
     * @param experimentSchema to define valid experiment descriptions
     */
    def execute(String schemaPath, String sampleSchema, String experimentSchema) {
        // get properties
        JsonParser metastatsProps = new JsonParser(configFile)
        Map credentials = null

        try {
            credentials = metastatsProps.parse()
        } catch (FileNotFoundException e) {
            e.printStackTrace()
        }

        JsonValidator validator = new JsonValidator(schemaPath)

        //define output classes
        FilterExperimentDataFileOutput fileOutputCreator = new TsvFileOutputCreator(projectCode)

        Map experimentalMappingInfo = getMapFromJson(experimentSchema)
        Map sampleMappingInfo = getMapFromJson(sampleSchema)

        PropertiesMapper mapper = new OpenBisMapper(experimentalMappingInfo, sampleMappingInfo)

        //define use case
        filterExperimentData = new FilterExperimentData(fileOutputCreator, mapper, validator)

        //define db classes
        setupDB(credentials)

        LOG.info("started metastats-cli")

        spec.requestProjectMetadata(projectCode)
    }

    /**
     * Creates a Map from the content of a json file
     * @param schema is a json file
     * @return either a map with the file content or null
     */
    static Map getMapFromJson(String schema) {
        JsonParser experimentProps = new JsonParser(schema)
        try {
            return experimentProps.parseStream()
        } catch (FileNotFoundException e) {
            e.printStackTrace()
            System.exit(-1)
        }
        return null
    }

    /**
     * Sets up the database connection based on the credentials given from the data of config file
     * @param credentials from the config file
     */
    def setupDB(Map<String, String> credentials) {
        int timeout = 30000
        OpenBisSession session = new OpenBisSession(credentials.get("user"), credentials.get("password"), credentials.get("server_url"), timeout)

        DatabaseGateway db = new OpenBisSearch(session)
        //define input for connector class
        RequestExperimentDataOutput experimentData = new PrepareMetaDataConnector(filterExperimentData)
        //define use case
        spec = new RequestExperimentData(db, experimentData)
    }

}
