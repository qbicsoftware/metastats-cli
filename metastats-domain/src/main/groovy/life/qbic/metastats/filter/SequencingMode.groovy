package life.qbic.metastats.filter

enum SequencingMode {
    PAIRED_END("paired-end"),
    SINGLE_END("single-end")

    private final String seqMode
    /**
     * Creates an enum based on the sequencing mode
     * @param mode sequencing mode 
     */
    SequencingMode(String mode) {
        this.seqMode = mode
    }

    /**
     * returns the sequencing mode as string
     * @return string of sequencing mode 
     */
    String getSeqMode() {
        return seqMode
    }
}
