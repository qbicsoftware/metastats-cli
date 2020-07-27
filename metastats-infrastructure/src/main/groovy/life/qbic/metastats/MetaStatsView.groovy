package life.qbic.metastats

/**
 * Handles the view of MetaStats
 *
 * This interface is responsible for how data can be viewed by the user which can be e.g. by the console
 *
 * @since: 1.0
 * @author: Jennifer Bödker
 *
 */
interface MetaStatsView {

    /**
     * Handles how text is displayed to the user via the console
     * @param text to be displayed to the user like status updates
     */
    void displayToConsole(String text)

}