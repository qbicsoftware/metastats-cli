package life.qbic.metastats.filter

class SequencingModeCalculator {

    static String calculateSequencingMode(String filename){
        String sequencingMode = ""
        String[] files = filename.split(",")

        boolean R1 = false
        boolean R2 = false
        files.each {file ->
            //Filenames should look like this: SampleName_S1_L001_R1_001.fastq.gz
            //or: SampleName_S1_L001_R1.fastq.gz
            if(file.contains("_R1_001.fastq")||file.contains("_R1.fastq")) R1 = true
            if(file.contains("_R2_001.fastq")||file.contains("_R2.fastq")) R2 = true
            if(!file.contains("fastq")) throw new IllegalFileType("File is not of type fastq: $file")
        }
        //has only R1
        if(!R2) sequencingMode = SequencingMode.SINGLE_END.seqMode
        //has R1 and R2
        if(R1 && R2) sequencingMode = SequencingMode.PAIRED_END.seqMode

        return sequencingMode
    }
}
