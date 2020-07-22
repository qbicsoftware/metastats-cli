package life.qbic.metastats.cli;

import life.qbic.cli.AbstractCommand;
import picocli.CommandLine;
import picocli.CommandLine.Command;

/**
 * Abstraction of command-line arguments that will be passed to {@link MetaStatsTool} at construction time.
 */
@Command(
        name = "MetaStats",
        description = "Command-line tool to create a metadata table for an openBIS project")
public class MetaStatsCommand extends AbstractCommand {

    @CommandLine.Option(names = {"-c", "--config"}, description = "config with user credentials.", required = true)
    String conf;

    @CommandLine.Option(names = {"-p", "--projectcode"}, description = "project code for openbis project.", required = true)
    String projectCode;

}