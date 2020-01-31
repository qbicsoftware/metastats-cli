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
import life.qbic.metastats.datamodel.MetaStatsExperiment
import life.qbic.metastats.datamodel.MetaStatsSample


class OpenBisSearch implements DatabaseGateway{


    Project project
    String projectCode
    IApplicationServerApi v3
    String sessionToken

    OpenBisSearch(IApplicationServerApi v3, String session){
        this.v3 = v3
        sessionToken = session
    }

    @Override
    void getProject(String projectCode) {

        def fetchOptions = new ProjectFetchOptions()

        def criteria = new ProjectSearchCriteria()
        criteria.withCode().thatEquals(projectCode)

        def experiment = new ExperimentFetchOptions()
        experiment.withProperties()
        experiment.withType()

        def sample = new SampleFetchOptions()
        sample.withProperties()
        sample.withType()
        sample.withChildrenUsing(sample)

        experiment.withSamplesUsing(sample)

        fetchOptions.withExperimentsUsing(experiment)

        SearchResult<Project> resProject = v3.searchProjects(sessionToken, criteria, fetchOptions)
        project = resProject.getObjects().get(0)
    }

    @Override
    List<MetaStatsExperiment> getExperimentsWithMetadata() {
        List<MetaStatsExperiment> experiments = []

        project.getExperiments().each { exp ->
            String type = exp.type.code
            List<String> samples = getSamplesForExperiment(exp)
            Map<String,String> props = exp.properties

            MetaStatsExperiment experiment = new MetaStatsExperiment(type,samples,props)

            experiments.add(experiment)
        }

        return experiments
    }

    List<String> getSamplesForExperiment(Experiment exp){
        List<String> sampleCodes = []

        exp.getSamples().each {
           sampleCodes.add(it.code)
        }
        return sampleCodes
    }

    @Override
    List<MetaStatsSample> getSamplesWithMetadata() {
        //find all biological samples with their children and return them as metastatssample objects
        //returning the biological sample as highest level sample is sufficient since all other samples
        //can be reached through its children
        List<MetaStatsSample> samples = findBiologicalEntity()
        assignPreparationSample(samples)

        return samples
    }

    def assignPreparationSample(List<MetaStatsSample> samples){
        setPreparationCodeForParent(samples)
        samples.each { entity ->
            entity.children.each { prepSample ->
                setPreparationCodeForChildren(prepSample.code, prepSample.children)
            }
        }
    }

    def setPreparationCodeForParent(List<MetaStatsSample> samples){
        samples.each { entity ->
            entity.children.each { prepSample ->
                entity.preparationSample << prepSample.code
            }
        }
    }

    def setPreparationCodeForChildren(String prepCode, List<MetaStatsSample> samples){

        samples.each { child ->
            child.preparationSample << prepCode
            if(child.children != null){
                setPreparationCodeForChildren(prepCode,child.children)
            }
        }
    }


    List<MetaStatsSample> findBiologicalEntity(){
        List samples = []

        project.getExperiments().each { exp ->
            //only do it for the Experiment with Biological Samples since from here all other samples can be found
            if(exp.getCode() =~ "Q[A-X0-9]{4}E2"){
                def openBisSamples = exp.getSamples()
                def exp_samples =  findBiologicalSamplesWithChildren(openBisSamples)
                samples = exp_samples
            }
        }

        return samples
    }


    List<MetaStatsSample> findBiologicalSamplesWithChildren(List<Sample> samples){

        List<MetaStatsSample> allSamples = []

        samples.each{sample ->
            //create sample object
            String code = sample.code
            String type = sample.type.code
            Map<String,String> properties = sample.getProperties()

            List<MetaStatsSample> children = findBiologicalSamplesWithChildren(sample.getChildren())

            allSamples << new MetaStatsSample(code,type,children,properties)
        }
        return allSamples
    }

}
