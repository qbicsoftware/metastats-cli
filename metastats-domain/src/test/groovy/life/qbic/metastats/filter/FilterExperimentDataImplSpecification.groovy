package life.qbic.metastats.filter

import life.qbic.metastats.datamodel.MetaStatsPackageEntry
import life.qbic.metastats.datamodel.MetaStatsSample
import spock.lang.Specification

class FilterExperimentDataImplSpecification extends Specification{


    def "add method extends map successfully"(){
        given:
        Map meta = new HashMap()
        meta.put("haha","hihi")

        Map props = new HashMap<String, String>()
        props.put("Q_Test","value")

        when:
        FilterExperimentDataImpl.add(meta,props)

        then:
        meta == ["Q_Test":"value","haha":"hihi"]
    }

    def "correctly transform metastats samples to package entries"(){
        given:
        Map props = new HashMap()
        props.put("samplePreparationId","QXXXXXX")
        props.put("species","value")
        props.put("sequencingDevice","value")
        props.put("fileName",["file2","file1","file3"])

        MetaStatsSample sample1 = new MetaStatsSample("QXXXXXX","Q_TEST_SAMPLE",props)
        sample1.addRelatives("QFSVIENTITY-1")

        when:
        List<MetaStatsPackageEntry> res = FilterExperimentDataImpl.createMetadataPackageEntries([sample1])

        then:
        res.get(0).entryId == "QXXXXXX"
        assert res.get(0).properties.get("fileName") == ["file2", "file1", "file3"]

    }

    def "filename must reflect either sampleName name or sequencingFacilityID"(){
        given:
        HashMap sampleProperties = ["samplePreparationId":"QXXX","sampleName":"IR1234","otherProp":"value","filename":"QXXXIR1234_1.fastq, QXXX.fastq, IR1234_3.fastq"]
        when:
        boolean res = FilterExperimentDataImpl.validFilenames(sampleProperties)
        then:
        res
    }

    def "invalid filenames are detected"(){
        given:
        HashMap sampleProperties = ["samplePreparationId":"QXXX","sampleName":"IR1234","otherProp":"value","filename":"234_1.fastq, Q2.fastq, _3.fastq"]
        when:
        boolean res = FilterExperimentDataImpl.validFilenames(sampleProperties)
        then:
        !res
    }

    def "missing files are detected"(){
        given:
        HashMap sampleProperties = ["samplePreparationId":"QXXX","sampleName":"IR1234","otherProp":"value","filename":""]
        when:
        boolean res = FilterExperimentDataImpl.validFilenames(sampleProperties)
        then:
        !res
    }


    def "More samples than files"(){
        //validateMetadataPackage()
        //todo
    }


}
