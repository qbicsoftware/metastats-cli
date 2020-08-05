package life.qbic.metastats.cli;

import life.qbic.cli.ToolExecutor;
import life.qbic.metastats.view.MetaStatsLogger;

/**
 * Entry point for the MetaStats-CLI application.
 *
 * The purpose of this class is to act as a bridge between the command line and the <i>real</i> implementation of a tool by using a {@link ToolExecutor}.
 *
 * @since: 1.0
 * @author: Jennifer Bödker
 *
 */
public class MetaStatsEntryPoint {

    private static final MetaStatsLogger LOG = new MetaStatsLogger(MetaStatsEntryPoint.class);

    /**
     * Main method.
     *
     * @param args the command-line arguments.
     */
    public static void main(final String[] args) {
        LOG.debug("Starting MetaStats tool");
        final ToolExecutor executor = new ToolExecutor();

        if (args.length == 4) {
            executor.invoke(MetaStatsTool.class, MetaStatsCommand.class, args);
        } else {
            LOG.error("Please define the commandline parameters, for more information use -h for help");
        }
    }
}
