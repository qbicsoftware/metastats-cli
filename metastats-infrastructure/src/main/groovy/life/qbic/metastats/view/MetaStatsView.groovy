package life.qbic.metastats.view

/**
 * Handles the view of MetaStats
 *
 * This interface is responsible the presentation of the generated data to the user which as of now is limited to console output
 *
 * @since: 1.0
 * @author: Jennifer Bödker
 *
 */
interface MetaStatsView {

    /**
     * Logs a debug message
     * @param message containing debug information
     */
    void debug(String message)

    /**
     * Logs a warning message
     * @param message containing warning information
     */
    void warning(String message)

    /**
     * Logs an error message
     * @param message containing error information
     */
    void error(String message)

    /**
     * Logs an info message
     * @param message containing unspecific information
     */
    void info(String message)

}
