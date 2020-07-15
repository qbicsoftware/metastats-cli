package life.qbic.metastats.filter

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
