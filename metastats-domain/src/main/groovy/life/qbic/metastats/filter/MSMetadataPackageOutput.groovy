package life.qbic.metastats.filter

import life.qbic.metastats.datamodel.MetaStatsPackageEntry

interface MSMetadataPackageOutput {

    /**
     * Creates the metasStats metadata package from the MetaStatsPackageEntries
     * @param samples List of MetaStatsPackageEntries containing the validated sample information necessary for the creation of a correct metadata sheet
     * @return
     */
    def createMetaStatsMetadataPackage(List<MetaStatsPackageEntry> samples)

    /**
     * Download the created metadata sheet to the user system
     * @return
     */
    def downloadMetadataPackage()

}
