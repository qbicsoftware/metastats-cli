package life.qbic.metastats.filter

enum SequencingMode {
    PAIRED_END("paired-end"),
    SINGLE_END("single-end")

    private final String seqMode
    SequencingMode(String mode){
        this.seqMode = mode
    }

    String getSeqMode(){
        return seqMode
    }
}