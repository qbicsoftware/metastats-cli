package life.qbic.metastats

import life.qbic.metastats.datamodel.MetaStatsSample
import life.qbic.metastats.request.OpenBisSearch
import life.qbic.metastats.request.OpenBisSession
import spock.lang.Specification

class OpenBisSearchSpecification extends Specification{



   /** def "find all biological entities"(){
        given:
        OpenBisSearch search = new OpenBisSearch(session.v3,session.sessionToken)
        search.getProject("QFSVI")

        when:
        def res = search.findBiologicalEntity()

        then:
        res.size() == 8
    }

    def "set preparation sample code to parent of preparation sample"(){
        given:
        OpenBisSearch search = new OpenBisSearch(session.v3,session.sessionToken)
        search.getProject("QFSVI")

        when:
        def res = search.findBiologicalEntity()
        search.setPreparationCodeForParent(res)

        then:
        res.each {
            def prepSample = it.children.get(0)
            it.preparationSample.get(0) == prepSample.code
        }
    }

    def "set preparation sample code to children of preparation sample"(){
        given:
        OpenBisSearch search = new OpenBisSearch(session.v3,session.sessionToken)
        search.getProject("QFSVI")

        when:
        def res = search.findBiologicalEntity()
        def prepSample = res.get(0).children.get(0)

        search.setPreparationCodeForChildren(prepSample.code,prepSample.children)

        then:
        prepSample.children.each {
            it.preparationSample.get(0) == "QFSVI001AQ"
        }
    }
 */

}
