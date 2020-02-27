package life.qbic.metastats

import life.qbic.metastats.datamodel.MetaStatsExperiment
import life.qbic.metastats.datamodel.MetaStatsSample
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
        def res = obm.mapSampleProperties(props)

        then:
        res == ["species":"value","sex":"value"]
        assert res.size() == 2
        assert props.size() == 3
    }

    def "mapping of biological sample properties is successful"(){
        given:
        Map props = new HashMap<String, String>()
        props.put("Q_PRIMARY_TISSUE","value")
        props.put("SEX","value")
        props.put("IgnoreThatKey","value")

        when:
        def res = obm.mapSampleProperties(props)

        then:
        res == ["tissue":"value"]
        assert res.size() == 1
    }

    def "mapping of test sample properties is successful"(){
        given:
        Map props = new HashMap<String, String>()
        props.put("Q_SAMPLE_TYPE","value")
        props.put("Q_SECONDARY_NAME","value")
        props.put("Q_RIN","value")

        when:
        def res = obm.mapSampleProperties(props)

        then:
        res == ["analyte":"value","sequencingFacilityId":"value","integrityNumber":"value"]
        assert res.size() == 3
    }

    def "mapping of sample runs properties is successful"(){
        given:
        Map props = new HashMap<String, String>()
        props.put("Q_SECONDARY_NAME","value")
        props.put("SEX","value")
        props.put("IgnoreThatKey","value")

        when:
        def res = obm.mapSampleProperties(props)

        then:
        res == ["sampleName":"value"]
        assert res.size() == 1
    }

    def "condition for experiment is mapped to correct sample"(){
        given:
        HashMap properties = new HashMap()
        String condition = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n" +
                "<qexperiment>\n" +
                "<technology_type name=\"Transcriptomics\"/>\n" +
                "<qfactors>\n" +
                "<qcategorical label=\"genotype\">\n" +
                "<qcatlevel value=\"wildtype\">\n" +
                "<entity_id>QFSVI016A5</entity_id>\n" +
                "</qcatlevel>\n" +
                "<qcatlevel value=\"mutant\">\n" +
                "<entity_id>QFSVIENTITY-1</entity_id>\n" +
                "</qcatlevel>\n" +
                "</qcategorical>\n" +
                "</qfactors>\n" +
                "</qexperiment>"
        properties.put("Q_EXPERIMENTAL_SETUP",condition)

        //MetaStatsExperiment experiment = new MetaStatsExperiment("Q_PROJECT_INFO", properties)
        MetaStatsSample sample1 = new MetaStatsSample("QXXXXXX","Q_TEST_SAMPLE",new HashMap<String, String>())
        sample1.addRelatives("QFSVIENTITY-1")

        MetaStatsSample sample2 = new MetaStatsSample("QXXXXXX","Q_TEST_SAMPLE",new HashMap<String, String>())
        sample2.addRelatives("QFSVI016A5")

        HashMap expected1 = new HashMap()
        expected1.put("condition:genotype","mutant")

        HashMap expected2 = new HashMap()
        expected2.put("condition:genotype","wildtype")

        when:
        def res = obm.mapExperimentProperties(properties,[sample1,sample2])

        then:
        sample1.properties == expected1
        assert sample2.properties == expected2
    }
}
