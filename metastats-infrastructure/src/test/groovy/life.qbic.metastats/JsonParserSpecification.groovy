package life.qbic.metastats

import life.qbic.metastats.io.JsonParser
import spock.lang.Specification

class JsonParserSpecification extends Specification{

    def "parsing properly"(){
        given:
        JsonParser parser= new JsonParser("model.schema.json")

        when:
        Map modelSchema = parser.parse()
        String[] res = modelSchema.get("properties").keySet() as String[]
        String[] req = modelSchema.get("required") as String[]

        then:
        res.size() == 13
        assert req == ["species"]
    }
}
