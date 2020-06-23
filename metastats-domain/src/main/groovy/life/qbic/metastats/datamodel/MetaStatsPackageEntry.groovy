package life.qbic.metastats.datamodel

class MetaStatsPackageEntry {

    final String preparationSampleId
    HashMap properties

    /**
     * Creates a MetaStatsPackageEntry with an ID and the corresponding properties
     * @param preparationSampleId specifies which sample is described
     * @param properties describe all fields specified in the model.schema.json that need to be part of the MetaStats output
     */
    MetaStatsPackageEntry(String preparationSampleId, HashMap properties) {
        this.preparationSampleId = preparationSampleId
        this.properties = properties
    }
}
