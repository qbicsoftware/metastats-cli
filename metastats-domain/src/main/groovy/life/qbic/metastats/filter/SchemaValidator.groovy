package life.qbic.metastats.filter

interface SchemaValidator {

    /**
     * Validates the metastats metadata contained in a map based on a given schema
     * @param valueMap
     * @return boolean indicating the success or failure of metadata validation against the predefined schema 
     */
    boolean validateMetaStatsMetadataPackage(Map valueMap)

}
