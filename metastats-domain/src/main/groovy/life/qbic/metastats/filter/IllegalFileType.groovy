package life.qbic.metastats.filter

/**
 * The class defines an exception for illegal file types
 *
 * If illegal file types are provided this exception should be thrown
 *
 * @since: 1.0
 * @author: Jennifer Bödker
 *
 */
class IllegalFileType extends IllegalArgumentException {

    /**
     * Creates an IllegalFileType exception with an error message
     * @param error Content of error message
     */
    IllegalFileType(String error) {
        super(error)
    }

    @Override
    String getMessage() {
        return super.getMessage()
    }
}
