package life.qbic.metastats.filter

class IllegalFileType extends IllegalArgumentException{

    /**
     * Creates an IlligalFileType exception with an error message
     * @param error
     */
    IllegalFileType(String error){
        super(error)
    }

    @Override
    String getMessage() {
        return super.getMessage()
    }
}
