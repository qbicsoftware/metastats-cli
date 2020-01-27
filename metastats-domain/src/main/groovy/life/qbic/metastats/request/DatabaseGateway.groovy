package life.qbic.metastats.request

import life.qbic.metastats.datamodel.MetaStatsExperiment
import life.qbic.metastats.datamodel.MetaStatsSample

interface DatabaseGateway {

    void getProject(String projectCode)
    List<MetaStatsExperiment> getExperimentMetadata()
    List<MetaStatsSample> getSampleMetadata() //HashMap<String,String>

}