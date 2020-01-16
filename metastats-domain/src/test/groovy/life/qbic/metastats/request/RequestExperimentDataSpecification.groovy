package life.qbic.metastats.request

import spock.lang.Specification

class RequestExperimentDataSpecification extends Specification{

    def mockedDatabaseGateway = Mock(DatabaseGateway)
    def mockedExperimentDataOutput = Mock(ExperimentDataOutput)
    def requestDataClass = new RequestExperimentData(mockedDatabaseGateway,mockedExperimentDataOutput)

    def "gives warning if there is no project (data) with given code"(){
        given:
        def projectCode = "XXXXX"

        when:
        def result = requestDataClass.requestProjectMetadata()

        then:
        thrown(ProjectNotFoundException)
    }

    def "gives no warning if there is a project (data) with given code"(){
        given:
        def projectCode = "QXXXX"

        when:
        def result = requestDataClass.requestProjectMetadata()

        then:
        noExceptionThrown()
    }

    def "detect right QBiC project codes"(){
        given:
        def projectCode = "QXXXX"

        when:
        boolean valid = requestDataClass.verifyQbicCode()

        then:
        valid
    }

    def "give a warning for wrong QBiC project codes"(){
        given:
        def projectCode = "XXXXX" //no leading Q
        def projectCode2 = "QXXXXX" //too long

        when:
        requestDataClass.verifyQbicCode()

        then:
        thrown(InvalidProjectCodeException)
    }

}
