package life.qbic.metastats.filter

import life.qbic.metastats.datamodel.MetaStatsPackageEntry

/**
 * The interface handles how the output data from the use case FilterExperimentData is transferred to a file
 *
 * It should be used when implementing a class that should obtain data from the FilterExperimentData class
 *
 * @since: 1.0
 * @author: Jennifer Bödker
 *
 */
interface FilterExperimentDataFileOutput {


    /**
     * Write the created metadata into a file and download it to the user system
     * @param entries with the metadata information of the project
     * @return
     */
    def write(List<MetaStatsPackageEntry> entries)

}
