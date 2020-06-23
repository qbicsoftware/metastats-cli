package life.qbic.metastats.filter

class IllegalFileType extends IllegalArgumentException{

    /**
     *
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
