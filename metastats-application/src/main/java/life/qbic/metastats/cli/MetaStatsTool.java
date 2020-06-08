package life.qbic.metastats.cli;

import life.qbic.cli.QBiCTool;
import life.qbic.metastats.MetaStatsController;
import life.qbic.metastats.MetaStatsPresenter;
import life.qbic.metastats.PrepareMetaData;
import life.qbic.metastats.fileCreator.TSVFileOutput;
import life.qbic.metastats.io.JsonParser;
import life.qbic.metastats.request.*;
import life.qbic.metastats.filter.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileNotFoundException;
import java.util.Map;

/**
 * Implementation of MetaStats-CLI. Its command-line arguments are contained in instances of {@link MetaStatsCommand}.
 */
public class MetaStatsTool extends QBiCTool<MetaStatsCommand> {

    private static final Logger LOG = LogManager.getLogger(MetaStatsTool.class);

    private static final String schemaPath = "/model.schema.json";
    private static final String sampleSchema = "openbisToMetastatsExperiment.json";
    private static final String experimentSchema = "openbisToMetastatsSample.json";


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

        MetaStatsController controller = new MetaStatsController(command.conf,command.projectCode);
        controller.execute(schemaPath,sampleSchema,experimentSchema);

    }
}