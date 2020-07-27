package life.qbic.metastats

import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

/**
 * Controls how data is presented
 *
 * This class is responsible for how the data is presented to the user. It acts as connector between the use cases and the view
 * and should be used to transfer data between these components.
 * It prepares the view model which can then be displayed by the view implementing {@link MetaStatsView}.
 *
 *
 * @since: 1.0
 * @author: Jennifer Bödker
 *
 */
class MetaStatsPresenter {

    private static final Logger LOG = LogManager.getLogger(MetaStatsPresenter.class)

    /**
     * Presents the output of metastats
     * @param creator interface that specifies how the file output is created e.g file type
     */
    MetaStatsPresenter() {
    }

}
