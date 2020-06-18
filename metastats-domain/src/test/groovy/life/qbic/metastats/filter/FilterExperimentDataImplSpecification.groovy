package life.qbic.metastats.filter

import life.qbic.metastats.datamodel.MetaStatsPackageEntry
import life.qbic.metastats.datamodel.MetaStatsSample
import spock.lang.Specification

class FilterExperimentDataImplSpecification extends Specification {


    def "add method extends map successfully"() {
        given:
        Map meta = new HashMap()
        meta.put("haha", "hihi")

        Map props = new HashMap<String, String>()
        props.put("Q_Test", "value")

        when:
        FilterExperimentDataImpl.add(meta, props)

        then:
        meta == ["Q_Test": "value", "haha": "hihi"]
    }

    def "correctly transform metastats samples to package entries"() {
        given:
        Map props = new HashMap()
        props.put("QBiC.Code", "QXXXXXX")
        props.put("Species", "value")
        props.put("IntegrityNumber", "1.0")
        props.put("FileName", ["file2", "file1", "file3"])

        MetaStatsSample sample1 = new MetaStatsSample("QXXXXXX", "Q_TEST_SAMPLE", props)
        sample1.addRelatives("QFSVIENTITY-1")

        when:
        List<MetaStatsPackageEntry> res = FilterExperimentDataImpl.createMetadataPackageEntries([sample1])

        then:
        res.get(0).entryId == "QXXXXXX"
        assert res.get(0).properties.get("FileName") == ["file2", "file1", "file3"]
        assert res.get(0).properties.get("IntegrityNumber") instanceof Double
    }

    def "filename must reflect either QBiC.Code name or SequencingFacilityID"() {
        given:
        HashMap sampleProperties = ["QBiC.Code": "QXXX", "SequencingFacilityId": "IR1234", "otherProp": "value", "Filename": "QXXXIR1234_1.fastq, QXXX.fastq, IR1234_3.fastq"]
        when:
        boolean res = FilterExperimentDataImpl.validFilenames(sampleProperties)
        then:
        res
    }

    def "invalid filenames are detected"() {
        given:
        HashMap sampleProperties = ["QBiC.Code": "QXXX", "SequencingFacilityId": "IR1234", "otherProp": "value", "Filename": "234_1.fastq, Q2.fastq, _3.fastq"]
        when:
        boolean res = FilterExperimentDataImpl.validFilenames(sampleProperties)
        then:
        !res
    }

    def "missing files are detected"() {
        given:
        HashMap sampleProperties = ["QBiC.Code": "QXXX", "SequencingFacilityId": "IR1234", "otherProp": "value", "Filename": ""]
        when:
        boolean res = FilterExperimentDataImpl.validFilenames(sampleProperties)
        then:
        !res
    }


    def "More samples than files"() {
        //validateMetadataPackage()
        //todo
    }

    def "Sort samples by QBiC.Code alphanumerically"(){
        given:
        MetaStatsPackageEntry entry = new MetaStatsPackageEntry("QFSVI009AM",["Filename":""])
        MetaStatsPackageEntry entry2 = new MetaStatsPackageEntry("QFSVI010AP",["Filename":""])
        MetaStatsPackageEntry entry3 = new MetaStatsPackageEntry("QFSVI012A7",["Filename":""])
        MetaStatsPackageEntry entry4 = new MetaStatsPackageEntry("QFSVI016A5",["Filename":""])

        when:
        def res =FilterExperimentDataImpl.sortEntries([entry3,entry2,entry,entry4])

        then:
        res == [entry,entry2,entry3,entry4]
    }

    def "Sort filenames alphanumerically"(){
        given:
        MetaStatsPackageEntry entry = new MetaStatsPackageEntry("QFSVI009AM",["Filename":"I16R019a02_01_S3_L001_R1_001.fastq.gz, I16R019a02_01_S3_L003_R1_001.fastq.gz, I16R019a02_01_S3_L004_R1_001.fastq.gz, I16R019a02_01_S3_L002_R1_001.fastq.gz"])
        MetaStatsPackageEntry entry2 = new MetaStatsPackageEntry("QFSVI010AP",["Filename":"I16R019b02_01_S5_L004_R1_001.fastq.gz, I16R019b02_01_S5_L003_R1_001.fastq.gz, I16R019b02_01_S5_L001_R1_001.fastq.gz, I16R019b02_01_S5_L002_R1_001.fastq.gz"])

        when:
        ArrayList<MetaStatsPackageEntry> res =FilterExperimentDataImpl.sortEntries([entry2,entry])

        then:
        res.get(0).properties.get("Filename") == "I16R019a02_01_S3_L001_R1_001.fastq.gz, I16R019a02_01_S3_L002_R1_001.fastq.gz, I16R019a02_01_S3_L003_R1_001.fastq.gz, I16R019a02_01_S3_L004_R1_001.fastq.gz"
        assert res.get(1).properties.get("Filename") == "I16R019b02_01_S5_L001_R1_001.fastq.gz, I16R019b02_01_S5_L002_R1_001.fastq.gz, I16R019b02_01_S5_L003_R1_001.fastq.gz, I16R019b02_01_S5_L004_R1_001.fastq.gz"
    }

}
