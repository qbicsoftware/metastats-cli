package life.qbic.metastats.filter

enum SequencingMode {
    PAIRED_END("paired-end"),
    SINGLE_END("single-end")

    private final String seqMode
    /**
     * Creates an enum based on the sequencing mode
     * @param mode
     */
    SequencingMode(String mode){
        this.seqMode = mode
    }

    /**
     * returns the sequencing mode as string
     * @return
     */
    String getSeqMode(){
        return seqMode
    }
}