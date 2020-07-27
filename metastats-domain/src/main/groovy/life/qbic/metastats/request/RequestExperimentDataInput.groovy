package life.qbic.metastats.request

/**
 * Describes classes that forward data into the RequestExperimentData use case
 *
 * This class restricts the input of the use case to defined method(s) and allows a controlled data flow from the
 * infrastructure module towards the domain module
 *
 * @since: 1.0
 * @author: Jennifer Bödker
 *
 */
interface RequestExperimentDataInput {

    /**
     * Requests the project information based on the project code
     * @param projectCode qbic code specifying the openbis project
     * @return
     */
    void requestProjectMetadata(String projectCode)

}