package life.qbic.metastats.fileCreator

import life.qbic.metastats.datamodel.MetaStatsPackageEntry

abstract class FileOutput {

    String fileEnding
    String projectCode

    /**
     * Creates a file for specific file format
     * @param fileEnding specifies the file format ending
     * @param projectCode specifies the code of the project
     */
    FileOutput(String fileEnding, String projectCode) {
        this.fileEnding = fileEnding
        this.projectCode = projectCode
    }

    /**
     * Creates the file content based on the given entry list
     * @param entries as list of the collected metadata
     * @return StringBuilder with the file content
     */
    abstract StringBuilder createFileContent(List<MetaStatsPackageEntry> entries)
}
