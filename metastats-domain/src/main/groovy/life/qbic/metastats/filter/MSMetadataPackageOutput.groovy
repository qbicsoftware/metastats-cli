package life.qbic.metastats.filter

import life.qbic.metastats.datamodel.MetaStatsPackageEntry

interface MSMetadataPackageOutput {

    def createMetaStatsMetadataPackage(List<MetaStatsPackageEntry> samples)

    def downloadMetadataPackage()

}