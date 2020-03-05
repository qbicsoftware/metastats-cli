package life.qbic.metastats.filter

import spock.lang.Specification

class SchemaValidatorSpecification extends Specification {

    def "parsing the schema correctly"(){
        given:
        Map map = new HashMap()

        SchemaValidator validator = new SchemaValidator(map)
    }
}
