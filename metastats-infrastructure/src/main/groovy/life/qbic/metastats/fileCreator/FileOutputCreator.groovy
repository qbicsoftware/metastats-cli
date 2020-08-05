package life.qbic.metastats.fileCreator

import life.qbic.metastats.datamodel.MetaStatsPackageEntry
import life.qbic.metastats.filter.FilterExperimentDataFileOutput
import life.qbic.metastats.view.MetaStatsLogger
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

/**
 * Defines how the output file is created
 *
 * This class handles how a list of MetaStatsEntries are transformed into a valid file format.
 * The file format needs to be determined by an implementing class such as how the file content looks like.
 * Furthermore this class handles the file name such as the output path of the file.
 *
 * @since: 1.0
 * @author: Jennifer Bödker
 *
 */
abstract class FileOutputCreator implements FilterExperimentDataFileOutput{

    String fileEnding
    String projectCode

    String fileName = "metastats-metadata-sheet"
    StringBuilder fileContent = new StringBuilder()

    String home = System.getProperty("user.home")
    String path = home + "/Downloads/" + projectCode + "_" + fileName + "." + fileEnding

    private static final MetaStatsLogger LOG = new MetaStatsLogger(FileOutputCreator.class)

    /**
     * Creates a file for specific file format
     * @param fileEnding specifies the file format ending
     * @param projectCode specifies the code of the project
     */
    FileOutputCreator(String fileEnding, String projectCode) {
        this.fileEnding = fileEnding
        this.projectCode = projectCode
    }

    /**
     * Creates the file content based on the given entry list
     * @param entries as list of the collected metadata
     * @return StringBuilder with the file content
     */
    abstract StringBuilder createFileContent(List<MetaStatsPackageEntry> entries)

    @Override
    def write(List<MetaStatsPackageEntry> entries) {
        fileContent = createFileContent(entries)

        File file = new File(path)
        file.write(fileContent.toString())

        LOG.info "Downloaded the metadata sheet of project $projectCode to:" + path
    }

}
