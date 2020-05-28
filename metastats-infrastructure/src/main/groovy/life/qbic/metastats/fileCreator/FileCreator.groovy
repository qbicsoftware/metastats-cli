package life.qbic.metastats.fileCreator

import life.qbic.metastats.datamodel.MetaStatsPackageEntry

interface FileCreator{

    StringBuilder createFileContent(List<MetaStatsPackageEntry> entries)
    String getFileEnding()
}
