package life.qbic.metastats.filter

class IllegalFileType extends IllegalArgumentException{

    IllegalFileType(String error){
        super(error)
    }

    @Override
    String getMessage() {
        return super.getMessage()
    }
}
