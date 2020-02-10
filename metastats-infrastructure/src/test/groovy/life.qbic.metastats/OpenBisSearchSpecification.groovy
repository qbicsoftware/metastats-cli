package life.qbic.metastats

import life.qbic.metastats.datamodel.MetaStatsSample
import life.qbic.metastats.request.OpenBisSearch
import life.qbic.metastats.request.OpenBisSession
import spock.lang.Specification

class OpenBisSearchSpecification extends Specification{

    /**def "Retrieve all Preparation Samples"(){
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
    }*/


}
