package life.qbic.metastats.filter


/**
 * Determines the sequencing mode of a sample
 *
 * The mode of sequencing can be determined from the file name.
 *
 * @since: 1.0
 * @author: Jennifer Bödker
 *
 */
class SequencingModeCalculator {

    /**
     * Calculates the sequencing mode based on all filenames of a sample.
     * If the files contain R1 and R2 then the mode is paired end. Else it is single end.
     * @param filename a string containing all filenames of a sample comma separated
     * @return String of determined sequencing mode
     */
    static SequencingMode calculateSequencingMode(List<String> filenames) {
        SequencingMode sequencingMode = SequencingMode.NA

        boolean R1 = false
        boolean R2 = false
        boolean invalid = false
        filenames.each { file ->
            //Filenames should look like this: SampleName_S1_L001_R1_001.fastq.gz
            //or: SampleName_S1_L001_R1.fastq.gz
            if (!file.contains("fastq")) invalid = true
            if (file.contains("_R1_001.fastq") || file.contains("_R1.fastq")) R1 = true
            if (file.contains("_R2_001.fastq") || file.contains("_R2.fastq")) R2 = true
        }
        if(invalid) return sequencingMode
        //has only R1
        if (!R2) sequencingMode = SequencingMode.SINGLE_END
        //has R1 and R2
        if (R1 && R2) sequencingMode = SequencingMode.PAIRED_END

        return sequencingMode
    }
}
