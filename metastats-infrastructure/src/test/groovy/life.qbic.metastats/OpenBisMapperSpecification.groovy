package life.qbic.metastats

import life.qbic.metastats.datamodel.Condition
import life.qbic.metastats.datamodel.MetaStatsExperiment
import life.qbic.metastats.datamodel.MetaStatsSample
import life.qbic.metastats.filter.OpenBisMapper
import life.qbic.metastats.io.JsonParser
import spock.lang.Specification

class OpenBisMapperSpecification extends Specification {

    private OpenBisMapper obm

    def setup() {
        URL url = OpenBisMapper.class.getClassLoader().getResource("openbisToMetastatsSample.json")
        // OpenBisMapper.class.classLoader.getResourceAsStream()
        JsonParser props = new JsonParser(url.getPath())
        Map mappingInfo = props.parse()

        URL url2 = OpenBisMapper.class.getClassLoader().getResource("openbisToMetastatsExperiment.json")
        // OpenBisMapper.class.classLoader.getResourceAsStream()
        JsonParser props2 = new JsonParser(url2.getPath())
        Map mappingInfo2 = props2.parse()

        obm = new OpenBisMapper(mappingInfo2, mappingInfo)
    }

    def "mapping of entity properties is successful"() {
        given:
        Map props = new HashMap<String, String>()
        props.put("Q_NCBI_ORGANISM", "value")
        props.put("SEX", "value")
        props.put("IgnoreThatKey", "value")

        when:
        def res = obm.mapSampleProperties(props)

        then:
        res.sort() == ["extractCode": "", "sampleName": "", "filename": "", "individual": "", "integrityNumber": "", "species": "value", "samplePreparationId": "", "sex": "value", "analyte": "", "tissue": "", "sequencingFacilityId": ""].sort()
    }

    def "mapping of biological sample properties is successful"() {
        given:
        Map props = new HashMap<String, String>()
        props.put("Q_PRIMARY_TISSUE", "value")
        props.put("SEX", "value")
        props.put("IgnoreThatKey", "value")

        when:
        def res = obm.mapSampleProperties(props)

        then:
        res.sort() == ["extractCode": "", "sampleName": "", "filename": "", "individual": "", "integrityNumber": "", "species": "", "samplePreparationId": "", "sex": "value", "analyte": "", "tissue": "value", "sequencingFacilityId": ""].sort()
    }

    def "mapping of test sample properties is successful"() {
        given:
        Map props = new HashMap<String, String>()
        props.put("Q_SAMPLE_TYPE", "value")
        props.put("Q_SECONDARY_NAME", "value")
        props.put("Q_RIN", "value")

        when:
        def res = obm.mapSampleProperties(props)

        then:
        res.sort() == ["extractCode": "", "sampleName": "", "filename": "", "individual": "", "integrityNumber": "", "species": "", "samplePreparationId": "", "sex": "", "analyte": "value", "tissue": "", "sequencingFacilityId": "value"].sort()
    }

    def "mapping of sample runs properties is successful"() {
        given:
        Map props = new HashMap<String, String>()
        props.put("Q_SECONDARY_NAME_Q_NGS_SINGLE_SAMPLE_RUN", "value")
        props.put("SEX", "value")
        props.put("IgnoreThatKey", "value")

        when:
        def res = obm.mapSampleProperties(props)

        then:
        res.sort() == ["extractCode": "", "sampleName": "value", "filename": "", "individual": "", "integrityNumber": "", "species": "", "samplePreparationId": "", "sex": "value", "analyte": "", "tissue": "", "sequencingFacilityId": ""].sort()
    }

    def "condition for experiment is mapped to correct sample"() {
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
                "<qcategorical label=\"test\">\n" +
                "<qcatlevel value=\"testtest\">\n" +
                "<entity_id>QFSVI016A5</entity_id>\n" +
                "</qcatlevel>\n" +
                "</qcategorical>\n" +
                "</qfactors>\n" +
                "</qexperiment>"
        properties.put("Q_EXPERIMENTAL_SETUP", condition)

        MetaStatsExperiment experiment = new MetaStatsExperiment("Q_PROJECT_DETAILS", properties, [])

        //MetaStatsExperiment experiment = new MetaStatsExperiment("Q_PROJECT_INFO", properties)
        MetaStatsSample sample1 = new MetaStatsSample("QXXXXXX", "Q_TEST_SAMPLE", new HashMap<String, String>())
        sample1.addRelatives("QFSVIENTITY-1")

        MetaStatsSample sample2 = new MetaStatsSample("QXXXXXX", "Q_TEST_SAMPLE", new HashMap<String, String>())
        sample2.addRelatives("QFSVI016A5")

        when:
        Map res1 = obm.mapExperimentToSample(experiment, sample1)
        Map res2 = obm.mapExperimentToSample(experiment, sample2)


        then:
        res1.get("condition") instanceof List<Condition>
        assert (res1.get("condition") as List).size() == 1
        assert (res2.get("condition") as List).size() == 2
    }

    def "mapping experiment correctly"() {
        given:
        Map props = new HashMap<String, String>()
        props.put("Q_SEQUENCER_DEVICE", "value")

        MetaStatsSample sample1 = new MetaStatsSample("QXXXXXX", "Q_TEST_SAMPLE", props)
        sample1.addRelatives("QFSVIENTITY-1")

        MetaStatsExperiment experiment = new MetaStatsExperiment("Q_OTHER", props, [])
        experiment.setSamples(["QXXXXXX"])

        when:
        def res = obm.mapExperimentToSample(experiment, sample1)

        then:
        res.sort() == ["condition": "", "sequencingDevice": "value"].sort()

    }
    //Q_SEQUENCER_DEVIC
}
