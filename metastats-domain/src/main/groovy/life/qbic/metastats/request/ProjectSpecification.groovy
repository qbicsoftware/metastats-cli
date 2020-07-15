package life.qbic.metastats.request

interface ProjectSpecification {

    /**
     * Requests the project information based on the project code
     * @param projectCode qbic code specifying the openbis project
     * @return
     */
    void requestProjectMetadata(String projectCode)

}