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
}
