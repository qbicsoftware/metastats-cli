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
import ch.ethz.sis.openbis.generic.asapi.v3.dto.vocabulary.VocabularyTerm
import ch.ethz.sis.openbis.generic.asapi.v3.dto.vocabulary.fetchoptions.VocabularyTermFetchOptions
import ch.ethz.sis.openbis.generic.asapi.v3.dto.vocabulary.search.VocabularyTermSearchCriteria
import ch.ethz.sis.openbis.generic.dssapi.v3.IDataStoreServerApi
import ch.ethz.sis.openbis.generic.dssapi.v3.dto.datasetfile.DataSetFile
import ch.ethz.sis.openbis.generic.dssapi.v3.dto.datasetfile.fetchoptions.DataSetFileFetchOptions
import ch.ethz.sis.openbis.generic.dssapi.v3.dto.datasetfile.search.DataSetFileSearchCriteria
import life.qbic.dataLoading.PostmanDataFilterer
import life.qbic.dataLoading.PostmanDataFinder
import life.qbic.metastats.datamodel.MetaStatsExperiment
import life.qbic.metastats.datamodel.MetaStatsSample
import org.apache.commons.lang.StringUtils
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger


class OpenBisSearch implements DatabaseGateway{


    Project project
    IApplicationServerApi v3
    IDataStoreServerApi dss
    String sessionToken

    OpenBisParser parser = new OpenBisParser()


    private static final Logger LOG = LogManager.getLogger(OpenBisSearch.class);

    OpenBisSearch(IApplicationServerApi v3, IDataStoreServerApi dss, String session){
        this.v3 = v3
        this.dss = dss
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
    List<MetaStatsExperiment> fetchExperimentsWithMetadata() {
        List<MetaStatsExperiment> experiments = []

        project.getExperiments().each { exp ->
            experiments.add(parser.createMetaStatsExperiment(exp))
        }

        return experiments
    }

    @Override
    List<MetaStatsSample> fetchSamplesWithMetadata() {
        List<MetaStatsSample> samples = fetchBiologicalEntity()

        return samples
    }



    List<MetaStatsSample> fetchBiologicalEntity(){
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

        //translate vocabularies to meaningfully
        translateVocabulary(prepSamples)
        //add filenames to sample
        addFile(prepSamples)

        return prepSamples
    }

    def translateVocabulary(List<MetaStatsSample> samples){
        samples.each {sample ->
            sample.properties.each {key, value ->
                if(key == "Q_NCBI_ORGANISM" || key == "Q_PRIMARY_TISSUE" || key == "Q_SEQUENCER_DEVICE"){
                    String vocabulary = fetchVocabulary(value)
                    //overwrite old key
                    sample.properties.put(key,vocabulary)
                }
            }
        }
    }

    def fetchVocabulary(String code){
        VocabularyTermSearchCriteria vocabularyTermSearchCriteria = new VocabularyTermSearchCriteria()
        vocabularyTermSearchCriteria.withCode().thatEquals(code)

        SearchResult<VocabularyTerm> vocabularyTermSearchResult = v3.searchVocabularyTerms(sessionToken, vocabularyTermSearchCriteria, new VocabularyTermFetchOptions())

        String term =  vocabularyTermSearchResult.objects.get(0).label

        return term
    }

    def addFile(List<MetaStatsSample> prepSamples){
        prepSamples.each {sample ->
            HashMap files = fetchDataSets(sample.code, "fastq")
            sample.properties << files
        }
    }

    HashMap<String,List> fetchDataSets(String sampleCode, String fileType){
        LOG.info "fetch DataSet for $sampleCode"

        PostmanDataFinder finder = new PostmanDataFinder(v3, dss, new PostmanDataFilterer(), sessionToken)

        HashMap allDataSets = new HashMap()

        finder.findAllDatasetsRecursive(sampleCode).each { dataSet ->
            DataSetFileSearchCriteria criteria = new DataSetFileSearchCriteria()
            criteria.withDataSet().withPermId().thatEquals(dataSet.permId.permId)
            criteria.withDataSet().withType()

            SearchResult<DataSetFile> result = dss.searchFiles(sessionToken, criteria, new DataSetFileFetchOptions())
            List<DataSetFile> files = result.getObjects()

            List<String> dataFiles = []

            for (DataSetFile file : files) {
                if (file.getPermId().toString().contains("." + fileType)
                        && !file.getPermId().toString().contains(".sha256sum")
                        && !file.getPermId().toString().contains("origlabfilename")) {
                    String[] path = file.getPermId().toString().split("/")
                    dataFiles << path[path.size() - 1]
                }
            }
            allDataSets.put(dataSet.type.code,dataFiles)
        }
        return allDataSets
    }


}
