package life.qbic.metastats.filter

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.github.fge.jackson.JsonLoader
import com.github.fge.jsonschema.core.report.ProcessingReport
import com.github.fge.jsonschema.main.JsonSchema
import com.github.fge.jsonschema.main.JsonSchemaFactory
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

class JsonValidator implements SchemaValidator{

    //TODO use model schema from https://github.com/qbicsoftware/metastats-object-model/blob/master/model.schema.json
    final JsonSchema jsonSchema

    private static final Logger LOG = LogManager.getLogger(JsonValidator.class)

    JsonValidator(String schema){

        JsonNode node =  JsonLoader.fromResource(schema)
        final JsonSchemaFactory factory = JsonSchemaFactory.byDefault()

        jsonSchema = factory.getJsonSchema(node)
    }

    @Override
    boolean validate(Map valueMap){
        //todo process conditions
        //todo handel rin

        ProcessingReport report
        ObjectMapper mapper = new ObjectMapper()
        JsonNode node = mapper.convertValue(valueMap, JsonNode.class)

        report = jsonSchema.validate(node)

        LOG.info "Json validation report start "
        LOG.info report
        LOG.info "Json validation report end "


        return !report.contains("error")
    }
    //todo handle conditions.. use further schema information, handel different types like integer for integrity number


}
