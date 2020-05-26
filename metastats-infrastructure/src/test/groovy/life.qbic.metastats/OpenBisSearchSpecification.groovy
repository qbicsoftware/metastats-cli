package life.qbic.metastats

import life.qbic.metastats.datamodel.MetaStatsExperiment
import life.qbic.metastats.datamodel.MetaStatsSample
import life.qbic.metastats.io.JsonParser
import life.qbic.metastats.request.OpenBisSearch
import life.qbic.metastats.request.OpenBisSession
import spock.lang.Specification

class OpenBisSearchSpecification extends Specification{

  /*  OpenBisSession session

    def setup(){
        def file = new File("/Users/jnnfr/private/credentials.json.properties")
        JsonParser props = new JsonParser(file.path)
        Map cred = (Map) props.parse()
        session = new OpenBisSession((String) cred.get("user"), (String) cred.get("password"), (String) cred.get("server_url"))
        println session
    }

    def "Fetch all files"(){
        given:
        OpenBisSearch search = new OpenBisSearch(session)
        search.getProject("QFSVI")

        when:
        HashMap res = search.fetchDataSets("QFSVI009AM","fastq")
        print res

        then:
        res.size() == 1
    }

    def "Retrieve all Preparation Samples"(){
        given:
        OpenBisSearch search = new OpenBisSearch(session)
        search.getProject("QFSVI")

        when:
        def res = search.fetchBiologicalEntity()

        then:
        res.size() == 8
    }

    def "Retrieve all Children of a Preparation Sample"(){
        given:
        OpenBisSearch search = new OpenBisSearch(session)
        search.getProject("QFSVI")

        when:
        List<MetaStatsSample> res = search.fetchBiologicalEntity()


        then:
        res.each {
            if(it.code == "QFSVI010AP"){
               // it.relatives.size() == 3 todo check size of properties?
            }
        }
    }

    def "Retrieve all Experiments with preparation sample"(){
        given:
        OpenBisSearch search = new OpenBisSearch(session)
        search.getProject("QFSVI")

        when:
        List<MetaStatsExperiment> res = search.fetchExperimentsWithMetadata()

        then:
        res.size() == 13
        res.each {
            if(it.type == "Q_PROJECT_DETAILS"){
                assert it.properties.size() == 1
            }
        }
    }*/

}
