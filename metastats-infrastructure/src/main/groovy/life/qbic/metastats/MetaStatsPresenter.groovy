package life.qbic.metastats

import life.qbic.metastats.datamodel.MetaStatsPackageEntry
import life.qbic.metastats.fileCreator.FileCreator
import life.qbic.metastats.filter.MSMetadataPackageOutput

class MetaStatsPresenter implements MSMetadataPackageOutput{

    FileCreator creator
    String fileName = "metastats-package"

    MetaStatsPresenter(FileCreator creator){
        this.creator = creator
    }

    @Override
    def createMetaStatsMetadataPackage(List<MetaStatsPackageEntry> entries) {
        StringBuilder fileContent = creator.createFileContent(entries)

        String home = System.getProperty("user.home")
        String path = home+"/Downloads/" + fileName + ".tsv"
        File file = new File(path)

        file.write(fileContent.toString())
    }

    @Override
    def downloadMetadataPackage() {
    }
}
