package life.qbic.metastats

import life.qbic.metastats.filter.OpenBisMapper
import spock.lang.Specification

class OpenBisMapperSpecification extends Specification{

    OpenBisMapper obm = new OpenBisMapper()

    def "mapping of entity properties is successful"(){
        given:
        Map props = new HashMap<String, String>()
        props.put("Q_NCBI_ORGANISM","value")
        props.put("SEX","value")
        props.put("IgnoreThatKey","value")

        when:
        def res = obm.mapEntityProperties(props)

        then:
        res == ["species":"value","sex":"value"]
        res.size() == 2
        props.size() == 3
    }

    def "mapping of biological sample properties is successful"(){
        given:
        Map props = new HashMap<String, String>()
        props.put("Q_PRIMARY_TISSUE","value")
        props.put("SEX","value")
        props.put("IgnoreThatKey","value")

        when:
        def res = obm.mapBioSampleProperties(props)

        then:
        res == ["tissue":"value"]
        res.size() == 1
    }

    def "mapping of test sample properties is successful"(){
        given:
        Map props = new HashMap<String, String>()
        props.put("Q_SAMPLE_TYPE","value")
        props.put("Q_SECONDARY_NAME","value")
        props.put("Q_RIN","value")

        when:
        def res = obm.mapTestSampleProperties(props)

        then:
        res == ["analyte":"value","sequencingFacilityId":"value","integrityNumber":"value"]
        res.size() == 3
    }

    def "mapping of sample runs properties is successful"(){
        given:
        Map props = new HashMap<String, String>()
        props.put("Q_SECONDARY_NAME","value")
        props.put("SEX","value")
        props.put("IgnoreThatKey","value")

        when:
        def res = obm.mapRunProperties(props)

        then:
        res == ["sampleName":"value"]
        res.size() == 1
    }
}
