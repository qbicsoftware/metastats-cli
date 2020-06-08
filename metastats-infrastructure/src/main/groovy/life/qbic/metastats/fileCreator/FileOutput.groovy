package life.qbic.metastats.fileCreator

import life.qbic.metastats.datamodel.MetaStatsPackageEntry

interface FileOutput {

    StringBuilder createFileContent(List<MetaStatsPackageEntry> entries)

    String getFileEnding()
}
