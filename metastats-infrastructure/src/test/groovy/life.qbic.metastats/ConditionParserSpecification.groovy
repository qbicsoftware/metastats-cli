package life.qbic.metastats


import life.qbic.metastats.filter.ConditionParser
import life.qbic.xml.manager.StudyXMLParser
import life.qbic.xml.properties.Property
import spock.lang.Specification

class ConditionParserSpecification extends Specification {

    String condition = """<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
            <qexperiment>
            <technology_type name="Transcriptomics"/>
            <qfactors>
            <qcategorical label="genotype">
            <qcatlevel value="wildtype">
            <entity_id>QFSVIXXXXY</entity_id>
            </qcatlevel>
            <qcatlevel value="mutant">
            <entity_id>QFSVIXXXXX</entity_id>
            </qcatlevel>
    </qcategorical>
    </qfactors>
    </qexperiment>
    """


    def "xml parsing retrieves correct conditions"() {
        given:
        ConditionParser parser = new ConditionParser()

        HashMap props = new HashMap()
        props.put("Q_EXPERIMENTAL_SETUP", condition)

        StudyXMLParser pars = new StudyXMLParser()
        pars.validate(condition)

        when:
        parser.parseProperties(props)
        List<Property> res = parser.getSampleConditions("QFSVIXXXXX")


        then:
        res.get(0).value == "mutant"
        assert res.get(0).label == "genotype"
    }
}
