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
import life.qbic.metastats.filter.ConditionParser
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger


class OpenBisSearch implements DatabaseGateway{


    Project project
    IApplicationServerApi v3
    String sessionToken
    OpenBisParser parser = new OpenBisParser()


    private static final Logger LOG = LogManager.getLogger(ConditionParser.class);

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
            experiments.add(parser.createMetaStatsExperiment(exp))
        }

        return experiments
    }

    @Override
    List<MetaStatsSample> getSamplesWithMetadata() {
        List<MetaStatsSample> samples = findBiologicalEntity()

        return samples
    }


    List<MetaStatsSample> findBiologicalEntity(){
        List prepSamples = []
        List<Experiment> experiments = project.experiments
        List<Sample> openBisSamples

       experiments.each { exp ->
           //only do it for the Experiment with Biological Samples since from here all other samples can be found
           if (exp.code == project.code+"E2" && exp.getSamples() != null){ //todo manchmal gibts e2 2x --> only consider exp with samples
               LOG.info "found $exp.type.code"

               LOG.info "fetch all samples from experiment"
               openBisSamples = exp.getSamples()

               prepSamples += parser.getPreparationSamples(openBisSamples)
           }
       }
        return prepSamples
    }




}
