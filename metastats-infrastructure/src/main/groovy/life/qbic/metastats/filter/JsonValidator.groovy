package life.qbic.metastats.filter

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.github.fge.jackson.JsonLoader
import com.github.fge.jsonschema.core.report.ProcessingReport
import com.github.fge.jsonschema.main.JsonSchema
import com.github.fge.jsonschema.main.JsonSchemaFactory
import life.qbic.metastats.view.MetaStatsLogger
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

/**
 * Handles the validation of JSON files
 *
 * Implements the SchemaValidator defined in the domain module. It handles how JSON schemas are loaded and JSON objects
 * validated.
 *
 * @since: 1.0
 * @author: Jennifer Bödker
 *
 */
class JsonValidator implements SchemaValidator {

    //TODO use model schema from external resource like github
    final JsonSchema jsonSchema

    private static final MetaStatsLogger LOG = new MetaStatsLogger(JsonValidator.class)

    /**
     * Creates a JsonSchema object based on a given schema
     * @param schema describing how the MetaStats output should look like
     */
    JsonValidator(File schema) {
        final JsonSchemaFactory factory = JsonSchemaFactory.byDefault()
        jsonSchema = factory.getJsonSchema("file:"+schema)
    }

    JsonValidator(String resource) {
        final JsonSchemaFactory factory = JsonSchemaFactory.byDefault()
        jsonSchema = factory.getJsonSchema("resource:"+resource)
    }

    @Override
    boolean validateMetaStatsMetadataPackage(Map valueMap) {

        ProcessingReport report
        ObjectMapper mapper = new ObjectMapper()
        JsonNode node = mapper.convertValue(valueMap, JsonNode.class)

        report = jsonSchema.validate(node, true)

        if (!report.isSuccess()) {
            LOG.warning "Validation failure! Check the log file to see a detailed validation error."
            LOG.info report as String
        }

        return report.isSuccess()

    }

}
