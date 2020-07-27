package life.qbic.metastats.filter

import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

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

    private static final Logger LOG = LogManager.getLogger(SequencingModeCalculator.class)

    /**
     * Calculates the sequencing mode based on all filenames of a sample.
     * If the files contain R1 and R2 then the mode is paired end. Else it is single end.
     * @param filename a string containing all filenames of a sample comma separated
     * @return String of determined sequencing mode 
     */
    static String calculateSequencingMode(String filename) {
        String sequencingMode = ""
        String[] files = filename.split(",")

        boolean R1 = false
        boolean R2 = false
        files.each { file ->
            //Filenames should look like this: SampleName_S1_L001_R1_001.fastq.gz
            //or: SampleName_S1_L001_R1.fastq.gz
            if (!file.contains("fastq")) throw new IllegalFileType("File is not of type fastq: $file")
            if (file.contains("_R1_001.fastq") || file.contains("_R1.fastq")) R1 = true
            if (file.contains("_R2_001.fastq") || file.contains("_R2.fastq")) R2 = true
        }
        //has only R1
        if (!R2) sequencingMode = SequencingMode.SINGLE_END.seqMode
        //has R1 and R2
        if (R1 && R2) sequencingMode = SequencingMode.PAIRED_END.seqMode
        //has not R2 and not R1
        if(!R1 && !R2) LOG.warn "Neither R1 nor R2 is contained within the sequencing name. Check for file naming conventions."


        return sequencingMode
    }
}
