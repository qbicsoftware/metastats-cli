package life.qbic.metastats.filter


import spock.lang.Specification

class SequencingModeCalculatorSpecification extends Specification {

    def "Single-End experiment is detected"() {
        given:
        List filenames = ["I16R019a02_01_S3_L001_R1_001.fastq.gz", "I16R019a02_01_S3_L002_R1_001.fastq.gz", "I16R019a02_01_S3_L003_R1_001.fastq.gz", "I16R019a02_01_S3_L004_R1_001.fastq.gz"]
        when:
        def res = SequencingModeCalculator.calculateSequencingMode(filenames)

        then:
        res.seqMode == "single-end"
    }

    def "Experiment which contains 'R2' in the filename is detected correctly as single-end"() {
        //if R2 is part of the sequencingfacility ID it should not be detected as paired-end experiment because of the id
        given:
        List filenames = ["I16R29a02_01_S3_L001_R1.fastq.gz", "I16R29a02_01_S3_L002_R1.fastq.gz", "I16R29a02_01_S3_L003_R1.fastq.gz", "I16R2a02_01_S3_L004_R1.fastq.gz"]
        when:
        def res = SequencingModeCalculator.calculateSequencingMode(filenames)

        then:
        res.seqMode == "single-end"
    }

    def "Invalid file type throws error"() {
        given:
        List filenames = ["I16R019a02_01_S3_L001_R1_001.sra.gz", "I16R019a02_01_S3_L002_R1_001.sra.gz", "I16R019a02_01_S3_L003_R1_001.sra.gz", "I16R019a02_01_S3_L004_R1_001.sra.gz"]
        when:
        def res = SequencingModeCalculator.calculateSequencingMode(filenames)
        then:
        res.seqMode == ""
    }

    def "Paired-End experiment is detected"() {
        given:
        List filenames = ["I16R019a02_01_S3_L001_R1_001.fastq.gz", "I16R019a02_01_S3_L002_R2_001.fastq.gz", "I16R019a02_01_S3_L001_R1_001.fastq.gz", "I16R019a02_01_S3_L002_R2_001.fastq.gz"]
        List filenames2 = ["I16R019a02_01_S3_L001_R1_001.fastq.gz", "I16R019a02_01_S3_L002_R2_001.fastq.gz", "I16R019a02_01_S3_L001_R1.fastq.gz", "I16R019a02_01_S3_L002_R2.fastq.gz"]


        when:
        def res = SequencingModeCalculator.calculateSequencingMode(filenames)
        def res2 = SequencingModeCalculator.calculateSequencingMode(filenames2)


        then:
        res.seqMode == "paired-end" && res2.seqMode == "paired-end"
    }

}
