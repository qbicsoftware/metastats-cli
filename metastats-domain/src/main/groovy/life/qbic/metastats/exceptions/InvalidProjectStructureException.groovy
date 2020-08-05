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
class InvalidProjectStructureException extends IllegalArgumentException {

    /**
     * Creates an IllegalFileType exception with an error message
     * @param error Content of error message
     */
    InvalidProjectStructureException(String error){
        super(error)
    }

    @Override
    String getMessage() {
        return super.getMessage()
    }
}
