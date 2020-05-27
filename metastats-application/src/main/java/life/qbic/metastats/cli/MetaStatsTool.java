package life.qbic.metastats.cli;

import life.qbic.cli.QBiCTool;
import life.qbic.metastats.MetaStatsPresenter;
import life.qbic.metastats.PrepareMetaData;
import life.qbic.metastats.io.JsonParser;
import life.qbic.metastats.request.*;
import life.qbic.metastats.filter.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.URL;
import java.util.Map;

/**
 * Implementation of MetaStats-CLI. Its command-line arguments are contained in instances of {@link MetaStatsCommand}.
 */
public class MetaStatsTool extends QBiCTool<MetaStatsCommand> {

    private static final Logger LOG = LogManager.getLogger(MetaStatsTool.class);

    /**
     * Constructor.
     *
     * @param command an object that represents the parsed command-line arguments.
     */
    public MetaStatsTool(final MetaStatsCommand command) {
        super(command);
    }

    @Override
    public void execute() {
        // get the parsed command-line arguments
        final MetaStatsCommand command = super.getCommand();
        // get properties
        LOG.debug(command.conf);
        JsonParser experimentProps = new JsonParser(command.conf);
        Map credentials = experimentProps.parse();

        JsonValidator validator = new JsonValidator("/model.schema.json");

        //define output classes
        MSMetadataPackageOutput metaStatsPresenter = new MetaStatsPresenter();

        experimentProps = new JsonParser("openbisToMetastatsExperiment.json");
        Map expMappingInfo = experimentProps.parseStream();

        JsonParser sampleProps = new JsonParser("openbisToMetastatsSample.json");
        Map sampleMappingInfo = sampleProps.parseStream();

        PropertiesMapper mapper = new OpenBisMapper(expMappingInfo, sampleMappingInfo);

        //define use case
        FilterExperimentData filter = new FilterExperimentDataImpl(metaStatsPresenter,mapper,validator);

        //define db classes
        OpenBisSession session = new OpenBisSession((String) credentials.get("user"),
                (String) credentials.get("password"),
                (String) credentials.get("server_url"));

        DatabaseGateway db = new OpenBisSearch(session);
        //define input for connector class
        ExperimentDataOutput experimentData = new PrepareMetaData(filter);
        //define use case
        ProjectSpecification spec = new RequestExperimentData(db,experimentData);

        LOG.info("started metastats-cli");

        spec.requestProjectMetadata(command.projectCode);

    }
}