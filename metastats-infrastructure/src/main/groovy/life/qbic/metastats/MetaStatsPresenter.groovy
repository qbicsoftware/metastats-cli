package life.qbic.metastats

import life.qbic.metastats.datamodel.MetaStatsPackageEntry
import life.qbic.metastats.filter.MSMetadataPackageOutput

class MetaStatsPresenter implements MSMetadataPackageOutput{

    @Override
    def createMetaStatsMetadataPackage(List<MetaStatsPackageEntry> entries) {
        StringBuilder fileContent = new StringBuilder()
        //create header with keywords and search for values in the samples
        entries.each {entry ->
            println entry.sampleName
            println entry.properties
        }
    }

    @Override
    def downloadMetadataPackage() {
    }
}
