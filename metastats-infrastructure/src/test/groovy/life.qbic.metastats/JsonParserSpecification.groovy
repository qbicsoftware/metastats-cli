package life.qbic.metastats

import life.qbic.metastats.io.JsonParser
import spock.lang.Specification

class JsonParserSpecification extends Specification {

    def "Right resource path is parsed without error"() {
        given:
        JsonParser parser = new JsonParser("model.schema.json")

        when:
        Map modelSchema = parser.parseStream()
        String[] res = modelSchema.get("properties").keySet() as String[]
        List<String> req = modelSchema.get("required") as List<String>

        then:
        res.size() == 14
        assert req.sort() == ["Species","SampleName"].sort()
    }

    def "Right file path is parsed without error"() {
        given:
        URL url = JsonParserSpecification.class.getClassLoader().getResource("model.schema.json")
        assert url != null
        JsonParser parser = new JsonParser(url.getPath())

        when:
        Map modelSchema = parser.parse()
        String[] res = modelSchema.get("properties").keySet() as String[]
        List<String> req = modelSchema.get("required") as List<String>

        then:
        res.size() == 14
        assert req.sort() == ["Species","SampleName"].sort()
    }

    def "Wrong file path throws error"() {
        given:
        JsonParser parser = new JsonParser("Modelschema.json")

        when:
        Map modelSchema = parser.parse()
        modelSchema.get("properties").keySet() as String[]
        modelSchema.get("required") as List<String>

        then:
        thrown(FileNotFoundException)
    }

    def "Wrong resource path throws error"() {
        given:
        JsonParser parser = new JsonParser("Modelschema.json")

        when:
        Map modelSchema = parser.parseStream()
        modelSchema.get("properties").keySet() as String[]
        modelSchema.get("required") as List<String>

        then:
        thrown(FileNotFoundException)
    }
}
