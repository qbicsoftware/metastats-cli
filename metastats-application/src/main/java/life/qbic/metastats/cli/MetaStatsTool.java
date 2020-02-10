package life.qbic.metastats.cli;

import life.qbic.cli.QBiCTool;
import life.qbic.metastats.MetaStatsPresenter;
import life.qbic.metastats.PrepareMetaData;
import life.qbic.metastats.request.*;
import life.qbic.metastats.filter.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
        ToolProperties props = new ToolProperties(command.conf);
        Map credentials = (Map) props.parse();

        //define output classes
        MSMetadataPackageOutput metaStatsPresenter = new MetaStatsPresenter();
        PropertiesMapper mapper = new OpenBisMapper();
        //define use case
        FilterExperimentData filter = new FilterExperimentDataImpl(metaStatsPresenter,mapper);

        //define db classes
        OpenBisSession session = new OpenBisSession((String) credentials.get("user"),
                (String) credentials.get("password"),
                (String) credentials.get("as_url"));
        //todo add dss_url
        DatabaseGateway db = new OpenBisSearch(session.getV3(),session.getSessionToken());
        //define input for connector class
        ExperimentDataOutput experimentData = new PrepareMetaData(filter);
        //define use case
        ProjectSpecification spec = new RequestExperimentData(db,experimentData);

        spec.requestProjectMetadata(command.projectCode);

    }
}