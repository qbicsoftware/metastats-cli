package life.qbic.metastats.filter

/**
 * The interface handles the validation of the MetaStats schema
 *
 * This interface enables the separation of the domain logic of MetaStats from an external library which is used
 * for validation of a schema.
 *
 * @since: 1.0
 * @author: Jennifer Bödker
 *
 */
interface SchemaValidator {

    /**
     * Validates the metastats metadata contained in a map based on a given schema
     * @param valueMap
     * @return boolean indicating the success or failure of metadata validation against the predefined schema 
     */
    boolean validateMetaStatsMetadataPackage(Map valueMap)

}
