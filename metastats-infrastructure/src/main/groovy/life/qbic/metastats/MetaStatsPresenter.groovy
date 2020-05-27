package life.qbic.metastats

import life.qbic.metastats.datamodel.MetaStatsPackageEntry
import life.qbic.metastats.fileCreator.FileCreator
import life.qbic.metastats.filter.MSMetadataPackageOutput

class MetaStatsPresenter implements MSMetadataPackageOutput{

    FileCreator creator
    String fileName = "metastats-package"
    StringBuilder fileContent = new StringBuilder()

    MetaStatsPresenter(FileCreator creator){
        this.creator = creator
    }

    @Override
    def createMetaStatsMetadataPackage(List<MetaStatsPackageEntry> entries) {
        fileContent = creator.createFileContent(entries)
    }

    @Override
    def downloadMetadataPackage() {
        String home = System.getProperty("user.home")
        String path = home+"/Downloads/" + fileName + ".tsv"

        File file = new File(path)

        file.write(fileContent.toString())
    }
}
