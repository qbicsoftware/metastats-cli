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
        sample.withParentsUsing(sample)

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
        List<MetaStatsSample> samples = findBiologicalEntity()
        //assignPreparationSample(entitySamples)

        return samples
    }


    List<MetaStatsSample> findBiologicalEntity(){
        List samples = []

        project.experiments.each { exp ->
            println exp.code
            //only do it for the Experiment with Biological Samples since from here all other samples can be found
            if(exp.code == project.code+"E2"){
                println "heeeeeeere"
                List<Sample> openBisSamples = exp.getSamples()
                List<MetaStatsSample> exp_samples = getPreparationSamples(openBisSamples)
               // samples = exp_samples
            }
        }
        return samples
    }


    def getPreparationSamples(List<Sample> samples){
        List<MetaStatsSample> allSamples = []
        List<String> visited = []

        samples.each{sample ->
            //create sample object
            String code = sample.code
            String type = sample.type.code
            println type+" is the sample type"

            if(type == "Q_TEST_SAMPLE"){
                println "found test sample"
                println code
                visited.add(sample.code)

                //get parent samples
                List parents = getAllParents(sample)
                //get children samples
                List children = getAllChildren(sample)

                //concatenate list and put is as children list
                allSamples << new MetaStatsSample(code, type, parents+children, properties)
            }
            else{
                //nullpointer??
                getPreparationSamples(sample.children)
            }
        }

        return allSamples
    }

    List<MetaStatsSample> getAllParents(Sample preparationSample){
        List parents = []
        println " error in parent"

        preparationSample.parents.each {parent->
            println "i am parent"+parent.type.code
            MetaStatsSample parentSample = new MetaStatsSample(parent.code, parent.type.code,parent.properties)
            parents.add(parentSample)

            if(parent.parents != null && parent.parents.get(0) instanceof Sample){
                parents += getAllParents(parent)
            }
        }

        return parents
    }

    List<MetaStatsSample> getAllChildren(Sample preparationSample){
        print "error in child"

        List children = []

        preparationSample.children.each {child->
            println "i am child"+child.type.code
            MetaStatsSample childSample = new MetaStatsSample(child.code, child.type.code,child.properties)
            children.add(childSample)

            if(child.children != null && child.children.get(0) instanceof Sample){
                children += getAllChildren(child)
            }
        }

        return children
    }


    /**
     *    List<MetaStatsSample> findBiologicalEntity(){*         List samples = []
     *
     *         project.getExperiments().each { exp ->
     *             //only do it for the Experiment with Biological Samples since from here all other samples can be found
     *             if(exp.getCode() =~ "Q[A-X0-9]{4}E2"){*                 def openBisSamples = exp.getSamples()
     *                 def exp_samples =  findAllSampleChildren(openBisSamples)
     *                 samples = exp_samples
     *}
     *         }
     *
     *         resamples
     *     }
     * def assignPreparationSample(List<MetaStatsSample> samples){
     setPreparationCodeForParent(samples)
     samples.each { entity ->
     entity.children.each { bioSample ->
     bioSample.children.each { prepSample ->
     setPreparationCodeForChildren(prepSample.code, prepSample.children)
     }
     }
     }
     }

     def setPreparationCodeForParent(List<MetaStatsSample> samples){
     samples.each { entity ->
     entity.children.each { bioSample ->
     bioSample.children.each { prepSample ->
     entity.preparationSample << prepSample.code
     }
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

     List<MetaStatsSample> findAllSampleChildren(List<Sample> biologicalEntitySamples){

     List<MetaStatsSample> allSamples = []

     biologicalEntitySamples.each{sample ->
     //create sample object
     String code = sample.code
     String type = sample.type.code
     println type+" is the sample type"


     Map<String,String> properties = sample.getProperties()

     List<MetaStatsSample> children = findAllSampleChildren(sample.getChildren())

     allSamples << new MetaStatsSample(code,type,children,properties)
     }
     return allSamples
     }
     }*/


}
