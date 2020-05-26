package life.qbic.metastats

import life.qbic.metastats.io.JsonParser
import spock.lang.Specification

class JsonParserSpecification extends Specification{

    def "parsing properly"(){
        given:
        URL url = JsonParserSpecification.class.getClassLoader().getResource("model.schema.json")
        assert url != null
        JsonParser parser = new JsonParser(url.getPath())

        when:
        Map modelSchema = parser.parse()
        String[] res = modelSchema.get("properties").keySet() as String[]
        List<String> req = modelSchema.get("required") as List<String>

        then:
        res.size() == 13
        assert req == ["species"]
    }
}
