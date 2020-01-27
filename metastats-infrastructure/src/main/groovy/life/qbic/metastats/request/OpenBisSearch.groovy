package life.qbic.metastats.request

import ch.ethz.sis.openbis.generic.asapi.v3.IApplicationServerApi
import ch.ethz.sis.openbis.generic.asapi.v3.dto.common.search.SearchResult
import ch.ethz.sis.openbis.generic.asapi.v3.dto.experiment.Experiment
import ch.ethz.sis.openbis.generic.asapi.v3.dto.experiment.fetchoptions.ExperimentFetchOptions
import ch.ethz.sis.openbis.generic.asapi.v3.dto.project.Project
import ch.ethz.sis.openbis.generic.asapi.v3.dto.project.fetchoptions.ProjectFetchOptions
import ch.ethz.sis.openbis.generic.asapi.v3.dto.project.search.ProjectSearchCriteria
import ch.ethz.sis.openbis.generic.asapi.v3.dto.sample.Sample
import ch.ethz.sis.openbis.generic.asapi.v3.dto.sample.fetchoptions.SampleFetchOptions
import ch.systemsx.cisd.common.spring.HttpInvokerUtils

import life.qbic.metastats.datamodel.MetaStatsExperiment
import life.qbic.metastats.datamodel.MetaStatsSample


class OpenBisSearch implements DatabaseGateway{

    String user
    String password
    String as_url
    String sessionToken
    IApplicationServerApi v3
    Project project
    String projectCode


    OpenBisSearch(String user, String password, as_url){
        this.user = user
        this.password = password
        this.as_url = as_url
    }

    //todo add these two methods into interface??
    def setupConnection(){
        // get a reference to AS API
        v3 = HttpInvokerUtils.createServiceStub(
                IApplicationServerApi.class, as_url + IApplicationServerApi.SERVICE_URL, 10000)
        sessionToken = v3.login(user, password)
    }

    def logout(){
        // logout to release the resources related with the session
        v3.logout(sessionToken);
    }


    @Override
    void getProject(String projectCode) {

        // invoke other API methods using the session token, for instance search for spaces
        def criteria = new ProjectSearchCriteria()
        criteria.withCode().thatEquals(projectCode)

        def fetchoptions = new ProjectFetchOptions()

        def experiment = new ExperimentFetchOptions()
        experiment.withProperties()
        experiment.withType()

        def sample = new SampleFetchOptions()
        sample.withProperties()
        sample.withType()
        sample.withChildrenUsing(sample)

        experiment.withSamplesUsing(sample)

        fetchoptions.withExperimentsUsing(experiment)

        SearchResult<Project> resProject = v3.searchProjects(sessionToken, criteria, fetchoptions)
        project = resProject.getObjects().get(0)
    }

    @Override
    List<MetaStatsExperiment> getExperimentMetadata() {
        List<MetaStatsExperiment> experiments = []

        project.getExperiments().each { exp ->
            String type = exp.type.code
            List<String> samples = getSamplesForExperiment(exp)
            Map<String,String> props = exp.properties

            MetaStatsExperiment experiment = new MetaStatsExperiment(type,samples,props)

            experiments.add(experiment)
        }

        return null
    }

    List<String> getSamplesForExperiment(Experiment exp){
        List<String> sampleCodes = []

        exp.getSamples().each {
            sampleCodes.add(it.code)
        }

        return sampleCodes
    }

    @Override
    List getSampleMetadata() {

        project.getExperiments().each { exp ->
            if(exp.getCode() =~ "Q[A-X0-9]{4}E1"){
                def openBisSamples = exp.getSamples()
                return findAllSamples(openBisSamples, exp.getType().getCode())
            }

        }
        println "No samples were found for the project $projectCode"
        return null
    }


    List<MetaStatsSample> findAllSamples(List<Sample> sample, String experimentType){

        List<MetaStatsSample> allSamples = []

        sample.each{
            //create sample object
            String code = it.code
            String type = it.type.code
            List<MetaStatsSample> children = null
            Map<String,String> properties = it.getProperties()

            if (it.getChildren() != null){
                children = findAllSamples(it.getChildren(),experimentType)
            }

            MetaStatsSample mss = new MetaStatsSample(code,type,children,properties)
            allSamples.add(mss)
        }

        allSamples
    }


}
