package life.qbic.metastats.request

interface DatabaseGateway {

    def setProject(String projectCode)
    def getExperimentMetadata()
    def getSampleMetadata()

}