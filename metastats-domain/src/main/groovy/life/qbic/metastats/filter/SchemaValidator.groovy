package life.qbic.metastats.filter

interface SchemaValidator {

    /**
     * Validates the metastats metadata contained in a map based on a given schema
     * @param valueMap
     * @return
     */
    boolean validateMetaStatsMetadataPackage(Map valueMap)

}