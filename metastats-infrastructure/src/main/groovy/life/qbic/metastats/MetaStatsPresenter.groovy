package life.qbic.metastats

import life.qbic.metastats.filter.MessageOutputPort
import life.qbic.metastats.view.MetaStatsLogger

/**
 * Controls how data is presented
 *
 * This class is responsible for how the data is presented to the user. It acts as connector between the use cases and the view
 * and should be used to transfer data between these components.
 * It prepares the view model which can then be displayed by the view implementing {@link life.qbic.metastats.view.MetaStatsView}.
 *
 *
 * @since: 1.0
 * @author: Jennifer Bödker
 *
 */
class MetaStatsPresenter implements MessageOutputPort{

    @Override
    void invokeOnError(String message, Class invokerClass) {
        MetaStatsLogger LOG = new MetaStatsLogger(invokerClass.class)
        LOG.error(message)
    }

    @Override
    void invokeOnInfo(String message, Class invokerClass) {
        MetaStatsLogger LOG = new MetaStatsLogger(invokerClass.class)
        LOG.info(message)
    }
}
