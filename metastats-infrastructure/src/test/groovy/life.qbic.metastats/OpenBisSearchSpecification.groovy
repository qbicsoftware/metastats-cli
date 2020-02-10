package life.qbic.metastats

import life.qbic.metastats.datamodel.MetaStatsExperiment
import life.qbic.metastats.datamodel.MetaStatsSample
import life.qbic.metastats.request.OpenBisSearch
import life.qbic.metastats.request.OpenBisSession
import life.qbic.metastats.request.ToolProperties
import spock.lang.Specification

class OpenBisSearchSpecification extends Specification{

    OpenBisSession session

    def setup(){
        def file = OpenBisSearchSpecification.class.getClassLoader().getResource("credentials.json.properties")
        ToolProperties props = new ToolProperties(file.path)
        Map cred = (Map) props.parse()
        session = new OpenBisSession((String) cred.get("user"), (String) cred.get("password"), (String) cred.get("as_url"))
    }

    def "Retrieve all Preparation Samples"(){
        given:
        OpenBisSearch search = new OpenBisSearch(session.v3,session.sessionToken)
        search.getProject("QFSVI")

        when:
        def res = search.findBiologicalEntity()

        then:
        res.size() == 8
    }

    def "Retrieve all Children of a Preparation Sample"(){
        given:
        OpenBisSearch search = new OpenBisSearch(session.v3,session.sessionToken)
        search.getProject("QFSVI")

        when:
        List<MetaStatsSample> res = search.findBiologicalEntity()


        then:
        res.each {
            if(it.code == "QFSVI010AP"){
                it.relatives.size() == 3
            }
        }
    }

    def "Retrieve all Experiments with preparation sample"(){
        given:
        OpenBisSearch search = new OpenBisSearch(session.v3,session.sessionToken)
        search.getProject("QFSVI")

        when:
        List<MetaStatsExperiment> res = search.getExperimentsWithMetadata()

        then:
        res.size() == 13
        res.each {
            if(it.type == "Q_PROJECT_DETAILS"){
                assert it.properties.size() == 1
            }
            println it.properties
            println it.type
        }
    }

}
