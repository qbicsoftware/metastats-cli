package life.qbic.metastats.datamodel

/**
 * Describes the characteristics of entries in the MetaStats output sheet
 *
 * This class is used as a data structure to represent entries of the output sheet. Each entry is associated with a
 * preparation sample from OpenBis. It shall facilitate the association of the preparation sample with the corresponding
 * metadata
 *
 * @since: 1.0
 * @author: Jennifer Bödker
 *
 */
class MetaStatsPackageEntry {

    final String preparationSampleId
    HashMap entryProperties

    /**
     * Creates a MetaStatsPackageEntry with an ID and the corresponding properties
     * @param preparationSampleId specifies which sample is described
     * @param entryProperties describe all fields specified in the model.schema.json that need to be part of the MetaStats output
     */
    MetaStatsPackageEntry(String preparationSampleId, HashMap entryProperties) {
        this.preparationSampleId = preparationSampleId
        this.entryProperties = entryProperties
    }
}
