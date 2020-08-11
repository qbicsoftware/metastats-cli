package life.qbic.metastats.exceptions

/**
 * Invalid project structures throw an InvalidProjectStructureException
 *
 * If an imported project does not follow the data model as described in doc/Openbis_Projectstructure.md this error needs to be thrown
 *
 * @since: 1.0
 * @author: Jennifer Bödker
 *
 */
class InvalidProjectStructureException extends RuntimeException {

    /**
     * Constructs a new InvalidProjectStrutureException with null as its detail message
     */
    InvalidProjectStructureException(){
        super()
    }

    /**
     * Creates an InvalidProjectStructureException with an error message
     * @param errorMessage is the detailed message
     */
    InvalidProjectStructureException(String errorMessage){
        super(errorMessage)
    }

    /**
     * Creates an InvalidProjectStructureException with an cause for the exception
     * @param cause for the exception which can be saved for later retrieval by the Throwable.getCause() method
     */
    InvalidProjectStructureException(Throwable cause){
        super(cause)
    }

    /**
     * Creates an InvalidProjectStructureException with an error message and the source of the error
     * @param errorMessage
     * @param cause is an throwable which describes the cause of the exception
     */
    InvalidProjectStructureException(String errorMessage, Throwable cause){
        super(errorMessage, cause)
    }

    /**
     * Constructs a new InvalidProjectStructureException with the specified detail message, cause, suppression enabled or disabled, and writable stack trace enabled or disabled.
     * @param errorMessage the detailed message
     * @param cause the cause for the error
     * @param enableSuppression states if suppression is enabled
     * @param writableStackTrace states if stacktrace is writable
     */
    InvalidProjectStructureException(String errorMessage, Throwable cause, boolean enableSuppression, boolean writableStackTrace){
        super(errorMessage,cause,enableSuppression,writableStackTrace)
    }

    @Override
    String getMessage() {
        return super.getMessage()
    }
}
