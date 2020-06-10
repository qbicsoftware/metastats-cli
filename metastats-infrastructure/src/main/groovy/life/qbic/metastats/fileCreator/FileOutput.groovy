package life.qbic.metastats.fileCreator

import life.qbic.metastats.datamodel.MetaStatsPackageEntry

abstract class FileOutput {

    String fileEnding
    String projectCode

    FileOutput(String fileEnding, String projectCode){
        this.fileEnding = fileEnding
        this.projectCode = projectCode
    }

    abstract StringBuilder createFileContent(List<MetaStatsPackageEntry> entries)
}
