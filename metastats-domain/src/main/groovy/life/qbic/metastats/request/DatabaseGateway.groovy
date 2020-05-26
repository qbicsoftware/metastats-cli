package life.qbic.metastats.request

import life.qbic.metastats.datamodel.MetaStatsExperiment
import life.qbic.metastats.datamodel.MetaStatsSample

interface DatabaseGateway {

    void getProject(String projectCode)
    List<MetaStatsExperiment> fetchExperimentsWithMetadata()
    List<MetaStatsSample> fetchSamplesWithMetadata() //HashMap<String,String>
    void logout()
    //List<MetaStatsSample> fetchFileNames() //HashMap<String,String>


}