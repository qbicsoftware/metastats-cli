package life.qbic.metastats.view


import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

/**
 * Logs messages to a logging file and to the console
 *
 * This class is a wrapper for the log4j log-manager. The dependency on the log-manager is restricted to this class.
 * It should be used to inform the user about actions in the software or documenting a process.
 *
 * @since: 1.0
 * @author: Jennifer Bödker
 *
 */
class MetaStatsLogger implements MetaStatsView{
    private static Logger LOG

    MetaStatsLogger(Class loggingClass){
        LOG = LogManager.getLogger(loggingClass.class)
    }

    void debug(String message){
        LOG.debug message
    }

    void warning(String message){
        LOG.warn message
    }

    void error(String message){
        LOG.error message
    }

    void info(String message){
        LOG.info message
    }
}
