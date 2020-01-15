package life.qbic.metastats.cli;

import life.qbic.cli.QBiCTool;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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

        // TODO: do something useful with the obtained command.
        //

    }

    // TODO: override the shutdown() method if you are implementing a daemon and want to take advantage of a shutdown hook for clean-up tasks
}