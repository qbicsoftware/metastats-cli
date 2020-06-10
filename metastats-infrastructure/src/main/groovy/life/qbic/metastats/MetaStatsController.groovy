package life.qbic.metastats

import life.qbic.metastats.fileCreator.TsvFileOutput
import life.qbic.metastats.filter.*
import life.qbic.metastats.io.JsonParser
import life.qbic.metastats.request.*
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

class MetaStatsController {

    private String configFile
    private String projectCode
    private ProjectSpecification spec
    private FilterExperimentData filterExperimentData

    private static final Logger LOG = LogManager.getLogger(MetaStatsController.class)

    MetaStatsController(String conf, String projectCode) {
        this.configFile = conf
        this.projectCode = projectCode
    }

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
        MSMetadataPackageOutput metaStatsPresenter = new MetaStatsPresenter(new TsvFileOutput(projectCode))

        Map experimentalMappingInfo = getMapFromJson(experimentSchema)
        Map sampleMappingInfo = getMapFromJson(sampleSchema)

        PropertiesMapper mapper = new OpenBisMapper(experimentalMappingInfo, sampleMappingInfo)

        //define use case
        filterExperimentData = new FilterExperimentDataImpl(metaStatsPresenter, mapper, validator)

        //define db classes
        setupDB(credentials)

        LOG.info("started metastats-cli")

        spec.requestProjectMetadata(projectCode)
    }

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

    def setupDB(Map<String, String> credentials) {
        OpenBisSession session = new OpenBisSession(credentials.get("user"), credentials.get("password"), credentials.get("server_url"))

        DatabaseGateway db = new OpenBisSearch(session)
        //define input for connector class
        ExperimentDataOutput experimentData = new PrepareMetaData(filterExperimentData)
        //define use case
        spec = new RequestExperimentData(db, experimentData)
    }

}
