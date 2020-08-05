package life.qbic.metastats.filter

/**
 * Class for transferring messages
 *
 * This interface describes how error or information messages need to be transferred. The implementing class should handle
 * the messages by displaying them to the user either via console or storing it in a log
 *
 * @since: 1.0
 * @author: Jennifer Bödker
 *
 */
interface MessageOutputPort {

    /**
     * Invokes an error with an error message and the class in which it occurs
     * @param message describing the error
     * @param invokerClass in which the error occurs
     */
    void invokeOnError(String message, Class invokerClass)

    /**
     * Invokes an info with an info message and the class in which it occurs
     * @param message describing containing the information
     * @param invokerClass in which triggers the information
     * @return
     */
    void invokeOnInfo(String message, Class invokerClass)

}
