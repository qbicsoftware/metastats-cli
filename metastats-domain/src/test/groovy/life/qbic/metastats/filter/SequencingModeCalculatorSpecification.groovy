package life.qbic.metastats.filter

import spock.lang.Specification

class SequencingModeCalculatorSpecification extends Specification {

    def "Single-End experiment is detected"() {
        given:
        String filenames = "I16R019a02_01_S3_L001_R1_001.fastq.gz, I16R019a02_01_S3_L002_R1_001.fastq.gz, I16R019a02_01_S3_L003_R1_001.fastq.gz, I16R019a02_01_S3_L004_R1_001.fastq.gz"
        when:
        def res = SequencingModeCalculator.calculateSequencingMode(filenames)

        then:
        res == "single-end"
    }

    def "Experiment which contains 'R2' in the filename is detected correctly as single-end"() {
        //if R2 is part of the sequencingfacility ID it should not be detected as paired-end experiment because of the id
        given:
        String filenames = "I16R29a02_01_S3_L001_R1.fastq.gz, I16R29a02_01_S3_L002_R1.fastq.gz, I16R29a02_01_S3_L003_R1.fastq.gz, I16R2a02_01_S3_L004_R1.fastq.gz"
        when:
        def res = SequencingModeCalculator.calculateSequencingMode(filenames)

        then:
        res == "single-end"
    }

    def "Invalid file type throws error"() {
        given:
        String filenames = "I16R019a02_01_S3_L001_R1_001.sra.gz, I16R019a02_01_S3_L002_R1_001.sra.gz, I16R019a02_01_S3_L003_R1_001.sra.gz, I16R019a02_01_S3_L004_R1_001.sra.gz"
        when:
        SequencingModeCalculator.calculateSequencingMode(filenames)
        then:
        thrown(IllegalFileType)
    }

    def "Paired-End experiment is detected"() {
        given:
        String filenames = "I16R019a02_01_S3_L001_R1_001.fastq.gz, I16R019a02_01_S3_L002_R2_001.fastq.gz, I16R019a02_01_S3_L001_R1_001.fastq.gz, I16R019a02_01_S3_L002_R2_001.fastq.gz"
        when:
        def res = SequencingModeCalculator.calculateSequencingMode(filenames)

        then:
        res == "paired-end"
    }
}
