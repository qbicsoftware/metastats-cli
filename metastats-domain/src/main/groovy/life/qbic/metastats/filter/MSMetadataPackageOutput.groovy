package life.qbic.metastats.filter

import life.qbic.metastats.datamodel.MetaStatsPackageEntry

interface MSMetadataPackageOutput {

    /**
     * Creates the metasStats metadata package from the MetaStatsPackageEntries
     * @param samples containing all validated information required a correct metadata sheet
     * @return
     */
    def createMetaStatsMetadataPackage(List<MetaStatsPackageEntry> samples)

    /**
     * Downloads the created metadata sheet to the users system
     * @return
     */
    def downloadMetadataPackage()

}