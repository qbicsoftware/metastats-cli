package life.qbic.metastats.cli;

import life.qbic.cli.QBiCTool;
import life.qbic.metastats.MetaStatsController;
import life.qbic.metastats.view.MetaStatsLogger;

/**
 * Implementation of MetaStats-CLI. Its command-line arguments are contained in instances of {@link MetaStatsCommand}.
 *
 * Used to start the CLI
 *
 * @since: 1.0
 * @author: Jennifer Bödker
 *
 */
public class MetaStatsTool extends QBiCTool<MetaStatsCommand> {

    private static final MetaStatsLogger LOG = new MetaStatsLogger(MetaStatsTool.class);

    private static final String schemaPath = "/schema/model.schema.json";
    private static final String sampleSchema = "openbisToMetastatsSample.json";
    private static final String experimentSchema = "openbisToMetastatsExperiment.json";


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

        MetaStatsController controller = new MetaStatsController(command.conf, command.projectCode);
        controller.execute(schemaPath, sampleSchema, experimentSchema);
    }
}