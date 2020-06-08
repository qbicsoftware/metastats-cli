package life.qbic.metastats


import life.qbic.metastats.filter.JsonValidator
import spock.lang.Specification

class JsonValidatorSpecification extends Specification {

    def "valid metadata returns true"() {
        given:
        JsonValidator validator = new JsonValidator("/model.schema.json")

        Map valueMap = ["samplePreparationId": "QFSVI123A1", "sequencingFacilityId": "facilityID", "sampleName": "testName", "individual": "QTESTENTITY-1", "species": "Homo sapiens",
                        "extractCode"        : "QTEST001AS", "sex": "", "tissue": "Vomit", "analyte": "RNA", "integrityNumber": 1, "conditions": [["label": "tumor", "value": "tumor"], ["label": "tumor", "value": "tumor"]],
                        "fileName"           : "thisIsAFile.txt, thisisAnotherFile.txt", "sequencingDevice": "Illumina MiSeq at MPIDB"]

        when:
        def res = validator.validateMetaStatsMetadataPackage(valueMap)

        then:
        res
    }

    def "wrong metadata returns false"() {
        given:
        JsonValidator validator = new JsonValidator("/model.schema.json")

        Map valueMap = ["samplePreparationId": "QFSVI123A1", "sequencingFacilityId": "facilityID", "sampleName": "testName", "individual": "QTESTENTITY-1",
                        "extractCode"        : "QTEST001AS", "sex": "", "tissue": "Vomit", "analyte": "RNA", "integrityNumber": 1, "conditions": [["label": "tumor", "value": "tumor"], ["label": "tumor", "value": "tumor"]],
                        "fileName"           : "thisIsAFile.txt, thisisAnotherFile.txt", "sequencingDevice": "Illumina MiSeq at MPIDB"]

        when:
        def res = validator.validateMetaStatsMetadataPackage(valueMap)

        then:
        !res
    }
}
