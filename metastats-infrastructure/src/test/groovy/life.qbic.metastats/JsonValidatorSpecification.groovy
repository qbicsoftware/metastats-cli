package life.qbic.metastats

import life.qbic.metastats.filter.JsonValidator
import spock.lang.Specification

class JsonValidatorSpecification extends Specification {

    def "valid metadata returns true"() {
        given:
        JsonValidator validator = new JsonValidator("/testSchema.json")

        Map valueMap = ["SamplePreparationId": "QFSVI123A1", "SequencingFacilityId": "facilityID", "SampleName": "testName", "Individual": "QTESTENTITY-1", "Species": "Homo sapiens",
                        "ExtractCode"        : "QTEST001AS", "Sex": "", "Tissue": "Vomit", "Analyte": "RNA", "IntegrityNumber": 1, "Condition": [["label": "tumor", "value": "tumor"], ["label": "tumor", "value": "tumor"]],
                        "FileName"           : "thisIsAFile.txt, thisisAnotherFile.txt", "SequencingDevice": "Illumina MiSeq at MPIDB"]

        when:
        def res = validator.validateMetaStatsMetadataPackage(valueMap)

        then:
        res
    }

    def "wrong metadata returns false"() {
        given:
        JsonValidator validator = new JsonValidator("/testSchema.json")

        //species missing
        Map valueMap = ["SamplePreparationId": "QFSVI123A1", "SequencingFacilityId": "facilityID", "SampleName": "testName", "Individual": "QTESTENTITY-1",
                        "ExtractCode"        : "QTEST001AS", "Sex": "", "Tissue": "Vomit", "Analyte": "RNA", "integrityNumber": 1, "Condition": [["label": "tumor", "value": "tumor"], ["label": "tumor", "value": "tumor"]],
                        "FileName"           : "thisIsAFile.txt, thisisAnotherFile.txt", "SequencingDevice": "Illumina MiSeq at MPIDB"]

        when:
        def res = validator.validateMetaStatsMetadataPackage(valueMap)

        then:
        !res
    }

    def "complex schema can be read correctly and detect correct values from a resource file "(){
        given:
        JsonValidator validator = new JsonValidator("/schema/parent.json")

        //species missing
        Map valueMap = ["MyBool": "true"]

        when:
        def res = validator.validateMetaStatsMetadataPackage(valueMap)

        then:
        res
    }

    def "complex schema can be read correctly and detect incorrect values from a resource file"(){
        given:
        JsonValidator validator = new JsonValidator("/schema/parent.json")

        Map valueMap = ["MyBool": "f"]

        when:
        def res = validator.validateMetaStatsMetadataPackage(valueMap)

        then:
        !res
    }

    def "complex schema can be read correctly and detect correct values from a file "(){
        given:
        String userdir = System.getProperty("user.dir")
        File file = new File(userdir+"/src/test/resources/schema/parent.json")
        JsonValidator validator = new JsonValidator(file)

        Map valueMap = ["MyBool": "true"]

        when:
        def res = validator.validateMetaStatsMetadataPackage(valueMap)

        then:
        res
    }

    def "complex schema can be read correctly and detect incorrect values from a file"(){
        given:
        String userdir = System.getProperty("user.dir")
        File file = new File(userdir+"/src/test/resources/schema/parent.json")
        JsonValidator validator = new JsonValidator(file)

        Map valueMap = ["MyBool": "f"]

        when:
        def res = validator.validateMetaStatsMetadataPackage(valueMap)

        then:
        !res
    }

}
