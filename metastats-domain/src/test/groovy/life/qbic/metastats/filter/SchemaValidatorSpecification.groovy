package life.qbic.metastats.filter

import com.fasterxml.jackson.core.JsonParser
import groovy.json.JsonSlurper
import spock.lang.Specification

class SchemaValidatorSpecification extends Specification {

    def "retrieving all properties from schema"(){
        given:
        InputStream stream = SchemaValidator.getClassLoader().getResourceAsStream("model.schema.json")
        Map map = (Map) new JsonSlurper().parseText(stream.text)

        SchemaValidator validator = new SchemaValidator(map)

        when:
        validator.propertiesFromSchema

        then:
        validator.metaStatsFields.size() == 13
    }

    def "retrieving required fields correctly"(){
        given:
        InputStream stream = SchemaValidator.getClassLoader().getResourceAsStream("model.schema.json")
        Map map = (Map) new JsonSlurper().parseText(stream.text)

        SchemaValidator validator = new SchemaValidator(map)

        when:
        validator.propertiesFromSchema

        then:
        validator.required.size() == 1
        assert validator.required == ["species"]
    }
}
