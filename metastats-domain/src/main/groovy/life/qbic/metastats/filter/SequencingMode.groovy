package life.qbic.metastats.filter

/**
 * Describes different sequencing modes
 *
 * A sequencing mode is either paired- or single-end. A controlled vocabulary prevents from unwanted terms and provides
 * a consistent naming.
 *
 * @since: 1.0
 * @author: Jennifer Bödker
 *
 */
enum SequencingMode {
    PAIRED_END("paired-end"),
    SINGLE_END("single-end"),
    NA("")

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
