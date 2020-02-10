package life.qbic.metastats.cli;

import ch.ethz.sis.openbis.generic.asapi.v3.IApplicationServerApi;
import life.qbic.cli.QBiCTool;
import life.qbic.metastats.request.*;
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
        /**OpenBisSession session =
        DatabaseGateway db = new OpenBisSearch(IApplicationServerApi,"")
        ProjectSpecification spec = new RequestExperimentData()

        MetaStatsController msc = new MetaStatsController(command.conf,command.projectCode);*/

    }

    // TODO: override the shutdown() method if you are implementing a daemon and want to take advantage of a shutdown hook for clean-up tasks
}