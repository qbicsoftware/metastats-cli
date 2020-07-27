package life.qbic.metastats.request

import spock.lang.Specification

class RequestExperimentDataSpecification extends Specification {

    def mockedDatabaseGateway = Mock(DatabaseGateway)
    def mockedExperimentDataOutput = Mock(RequestExperimentDataOutput)
    def requestDataClass = new RequestExperimentData(mockedDatabaseGateway, mockedExperimentDataOutput)

    /** def "gives warning if there is no project (data) with given code"(){given:
     def projectCode = "XXXXX"

     when:
     def result = requestDataClass.requestProjectMetadata()

     then:
     thrown(ProjectNotFoundException)}def "gives no warning if there is a project (data) with given code"(){given:
     def projectCode = "QXXXX"

     when:
     def result = requestDataClass.requestProjectMetadata()

     then:
     noExceptionThrown()}*/

    def "detect valid QBiC project codes"() {
        given:
        def projectCode = "QXXXX"

        when:
        boolean valid = requestDataClass.verifyQbicCode(projectCode)

        then:
        valid
    }

    def "invalid QBiC project codes results in warning"() {
        given:
        def projectCode = "XXXXX" //no leading Q
        def projectCode2 = "QXXXXX" //too long

        when:
        def res1 = requestDataClass.verifyQbicCode(projectCode)
        def res2 = requestDataClass.verifyQbicCode(projectCode2)

        then:
        !res1
        !res2
    }

}
