package life.qbic.metastats.request

interface DatabaseGateway {

    void setProject(String projectCode)
    HashMap<String,String> getExperimentMetadata()
    HashMap<String,String> getSampleMetadata()

}