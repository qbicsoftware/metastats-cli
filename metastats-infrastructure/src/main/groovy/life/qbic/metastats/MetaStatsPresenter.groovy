package life.qbic.metastats

import life.qbic.metastats.datamodel.MetaStatsPackageEntry
import life.qbic.metastats.fileCreator.FileCreator
import life.qbic.metastats.filter.MSMetadataPackageOutput
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

class MetaStatsPresenter implements MSMetadataPackageOutput{

    FileCreator creator
    String fileName = "metastats-metadata-sheet"
    StringBuilder fileContent = new StringBuilder()

    String home = System.getProperty("user.home")
    String path = home+"/Downloads/" + fileName + "."+creator.fileEnding


    private static final Logger LOG = LogManager.getLogger(MetaStatsPresenter.class)

    MetaStatsPresenter(FileCreator creator){
        this.creator = creator
    }

    @Override
    def createMetaStatsMetadataPackage(List<MetaStatsPackageEntry> entries) {
        fileContent = creator.createFileContent(entries)
    }

    //todo create the name based on the project name
    @Override
    def downloadMetadataPackage() {

        File file = new File(path)
        file.write(fileContent.toString())

        LOG.info "Downloaded the metadata sheet of project XXX to:" + path

    }
}
