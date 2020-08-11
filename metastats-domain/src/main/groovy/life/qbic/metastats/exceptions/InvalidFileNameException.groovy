package life.qbic.metastats.exceptions

/**
 * Invalid file names throw an exception
 *
 * If a filename does not follow the naming conventions this exception should be thrown
 *
 * @since: 1.0
 * @author: Jennifer Bödker
 *
 */
class InvalidFileNameException extends RuntimeException{

    /**
     * Constructs a new InvalidFileNameException with null as its detail message
     */
    InvalidFileNameException(){
        super()
    }

    /**
     * Creates an InvalidFileNameException exception with an error message
     * @param errorMessage Content of error message
     */
    InvalidFileNameException(String errorMessage){
        super(errorMessage)
    }

    /**
     * Creates an InvalidFileNameException exception with a cause
     * @param cause is the cause for the error
     */
    InvalidFileNameException(Throwable cause){
        super(cause)
    }

    /**
     * Creates an InvalidFileNameException exception with an error message with the source of the error
     * @param errorMessage describes the error details
     * @param err describes the source of the error
     */
    InvalidFileNameException(String errorMessage, Throwable err){
        super(errorMessage, err)
    }

    /**
     * Creates an InvalidFileNameException exception with an error message with the source of the error
     * @param errorMessage describes the error details
     * @param cause describes the reason of the error
     * @param enableSuppression states if suppression is enabled
     * @param writableStackTrace states if stacktrace is writable
     */
    InvalidFileNameException(String errorMessage, Throwable cause, boolean enableSuppression, boolean writableStackTrace){
        super(errorMessage, cause, enableSuppression, writableStackTrace)
    }

    @Override
    String getMessage() {
        return super.getMessage()
    }
}
