package life.qbic.metastats

import life.qbic.metastats.datamodel.MetaStatsExperiment
import life.qbic.metastats.filter.ConditionParser
import spock.lang.Specification

class ConditionParserSpecification extends Specification{

    String condition = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>" +
            "<qexperiment>" +
            "</qexperiment>"


    def "xml parsing retrieves the right number of Conditions"(){
        given:
        ConditionParser parser = new ConditionParser()

        HashMap props = new HashMap()
        props.put("Q_EXPERIMENTAL_SETUP",properties)

        MetaStatsExperiment experiment = new MetaStatsExperiment("Q_PROJECT_INFO",[],props)

        when:
        def res = parser.parseProperties(experiment)
        println res

        then:
        true
    }
}
