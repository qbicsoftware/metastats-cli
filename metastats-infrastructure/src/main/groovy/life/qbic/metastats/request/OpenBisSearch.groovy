package life.qbic.metastats.request

import ch.ethz.sis.openbis.generic.asapi.v3.IApplicationServerApi
import ch.ethz.sis.openbis.generic.asapi.v3.dto.common.search.SearchResult
import ch.ethz.sis.openbis.generic.asapi.v3.dto.dataset.DataSet
import ch.ethz.sis.openbis.generic.asapi.v3.dto.dataset.fetchoptions.DataSetFetchOptions
import ch.ethz.sis.openbis.generic.asapi.v3.dto.experiment.Experiment
import ch.ethz.sis.openbis.generic.asapi.v3.dto.experiment.fetchoptions.ExperimentFetchOptions
import ch.ethz.sis.openbis.generic.asapi.v3.dto.project.Project
import ch.ethz.sis.openbis.generic.asapi.v3.dto.project.fetchoptions.ProjectFetchOptions
import ch.ethz.sis.openbis.generic.asapi.v3.dto.project.search.ProjectSearchCriteria
import ch.ethz.sis.openbis.generic.asapi.v3.dto.sample.Sample
import ch.ethz.sis.openbis.generic.asapi.v3.dto.sample.fetchoptions.SampleFetchOptions
import ch.ethz.sis.openbis.generic.asapi.v3.dto.sample.search.SampleSearchCriteria
import ch.ethz.sis.openbis.generic.asapi.v3.dto.vocabulary.VocabularyTerm
import ch.ethz.sis.openbis.generic.asapi.v3.dto.vocabulary.fetchoptions.VocabularyTermFetchOptions
import ch.ethz.sis.openbis.generic.asapi.v3.dto.vocabulary.search.VocabularyTermSearchCriteria
import ch.ethz.sis.openbis.generic.dssapi.v3.IDataStoreServerApi
import ch.ethz.sis.openbis.generic.dssapi.v3.dto.datasetfile.DataSetFile
import ch.ethz.sis.openbis.generic.dssapi.v3.dto.datasetfile.fetchoptions.DataSetFileFetchOptions
import ch.ethz.sis.openbis.generic.dssapi.v3.dto.datasetfile.search.DataSetFileSearchCriteria
import life.qbic.metastats.datamodel.MetaStatsExperiment
import life.qbic.metastats.datamodel.MetaStatsSample
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger


class OpenBisSearch implements DatabaseGateway {

    Project project
    IApplicationServerApi v3
    IDataStoreServerApi dss
    String sessionToken
    private OpenBisSession session

    OpenBisParser parser = new OpenBisParser()


    private static final Logger LOG = LogManager.getLogger(OpenBisSearch.class)

    /**
     * Creates an openbis search for a given session
     * @param session used to query openbis
     */
    OpenBisSearch(OpenBisSession session) {
        this.session = session
        this.v3 = session.v3
        this.dss = session.dss
        sessionToken = session.sessionToken
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

        if (resProject.getObjects().size() == 0) {
            LOG.warn "Project $projectCode not found"
            System.exit(-1)
        }

        project = resProject.getObjects().get(0)
    }

    @Override
    List<MetaStatsExperiment> fetchExperimentsWithMetadata() {
        List<MetaStatsExperiment> experiments = []

        project.getExperiments().each { exp ->
            experiments.add(parser.createMetaStatsExperiment(exp))
        }

        translateExperimentVocabulary(experiments)

        return experiments
    }

    @Override
    List<MetaStatsSample> fetchSamplesWithMetadata() {
        List<MetaStatsSample> samples = fetchBiologicalEntity()

        return samples
    }

    @Override
    void logout() {
        session.logout()
    }

    /**
     * Retrieves all samples on the level of biological entities (Q_BIOLOGICAL_ENTITY)
     * @return a list of metastats samples
     */
    List<MetaStatsSample> fetchBiologicalEntity() {
        List prepSamples = []
        List<Experiment> experiments = project.experiments
        List<Sample> openBisSamples

        experiments.each { exp ->
            //only do it for the Experiment with Biological Samples since from here all other samples can be found
            if (exp.type.code == "Q_EXPERIMENTAL_DESIGN" && exp.getSamples() != null) {
                //todo manchmal gibts e2 2x --> only consider exp with samples
                LOG.info "found $exp.type.code"

                LOG.info "fetch all samples from experiment"
                openBisSamples = exp.getSamples()

                prepSamples += parser.getPreparationSamples(openBisSamples)
            }
        }

        //translate vocabularies to meaningfully
        translateSampleVocabulary(prepSamples)
        //add filenames to sample
        addFile(prepSamples)

        return prepSamples
    }

    /**
     * Translates the vocabulary of a given list of samples
     * @param samples containing properties that require translation e.g Q_NCBI_ORGANISM
     */
    void translateSampleVocabulary(List<MetaStatsSample> samples) {
        samples.each { sample ->
            sample.properties.each { key, value ->
                if (key == "Q_NCBI_ORGANISM" || key == "Q_PRIMARY_TISSUE") {
                    String vocabulary = fetchVocabulary(value)
                    //overwrite old key
                    sample.properties.put(key, vocabulary)
                }
            }
        }
    }

    /**
     * Translates the Experiment vocabulary
     * @param experiments containing properties that require translation e.g Q_SEQUENCER_DEVICE
     */
    void translateExperimentVocabulary(List<MetaStatsExperiment> experiments) {
        experiments.each { experiment ->
            experiment.properties.each { key, value ->
                if (key == "Q_SEQUENCER_DEVICE") {
                    String vocabulary = fetchVocabulary(value)
                    //overwrite old key
                    experiment.properties.put(key, vocabulary)
                }
            }
        }
    }

    /**
     * Fetches the vocabulary that needs to be translated from openbis
     * @param code
     * @return translated term
     */
    String fetchVocabulary(String code) {
        VocabularyTermSearchCriteria vocabularyTermSearchCriteria = new VocabularyTermSearchCriteria()
        vocabularyTermSearchCriteria.withCode().thatEquals(code)

        SearchResult<VocabularyTerm> vocabularyTermSearchResult = v3.searchVocabularyTerms(sessionToken, vocabularyTermSearchCriteria, new VocabularyTermFetchOptions())

        String term = vocabularyTermSearchResult.objects.get(0).label

        return term
    }

    /**
     * Fetches and adds files to a given list of samples
     * @param prepSamples to which corresponding files are added
     */
    void addFile(List<MetaStatsSample> prepSamples) {
        prepSamples.each { sample ->
            HashMap files = fetchDataSets(sample.sampleCode, "fastq")
            sample.properties << files
        }
    }

    /**
     * Fetches datasets of a given filetype for a sample specified by its code
     * @param sampleCode specifying the sample for which the DS shall be fetched
     * @param fileType specifying which files are searched
     * @return a maps of found datasets with their openbis type
     */
    HashMap<String, String> fetchDataSets(String sampleCode, String fileType) {
        LOG.info "fetch DataSet for $sampleCode"

        HashMap allDataSets = new HashMap()

        StringBuilder dataFiles = new StringBuilder("")
        String datasetType = ""

        findAllDatasetsRecursive(sampleCode).each { dataSet ->
            DataSetFileSearchCriteria criteria = new DataSetFileSearchCriteria()
            criteria.withDataSet().withPermId().thatEquals(dataSet.permId.permId)
            criteria.withDataSet().withType()

            SearchResult<DataSetFile> result = dss.searchFiles(sessionToken, criteria, new DataSetFileFetchOptions())
            List<DataSetFile> files = result.getObjects()

            for (DataSetFile file : files) {
                if (file.getPermId().toString().contains("." + fileType)
                        && !file.getPermId().toString().contains(".sha256sum")
                        && !file.getPermId().toString().contains("origlabfilename")
                        && !file.getPermId().toString().contains(".csv")
                        && !file.getPermId().toString().contains(".txt")) {
                    String[] path = file.getPermId().toString().split("/")
                    if (!dataFiles.contains(path[path.size() - 1])) dataFiles << path[path.size() - 1] + ", "
                    datasetType = dataSet.type.code
                }
            }
        }

        if (dataFiles.toString() != "") {
            dataFiles.delete(dataFiles.length() - 2, dataFiles.length())
            allDataSets.put(datasetType, dataFiles)
        }

        return allDataSets
    }

    /**
     * Searches for all related datasets of a sample with a recursion
     * @param sampleId specifying the sample for which DS are searched
     * @return list of found datasets
     */
    List<DataSet> findAllDatasetsRecursive(final String sampleId) {
        SampleSearchCriteria criteria = new SampleSearchCriteria()
        criteria.withCode().thatEquals(sampleId)

        // tell the API to fetch all descendants for each returned sample
        SampleFetchOptions fetchOptions = new SampleFetchOptions()
        DataSetFetchOptions dsFetchOptions = new DataSetFetchOptions()
        dsFetchOptions.withType()
        fetchOptions.withChildrenUsing(fetchOptions)
        fetchOptions.withDataSetsUsing(dsFetchOptions)
        SearchResult<Sample> result = v3.searchSamples(sessionToken, criteria, fetchOptions)

        List<DataSet> foundDatasets = new ArrayList<>()

        for (Sample sample : result.getObjects()) {
            // add the datasets of the sample itself
            foundDatasets.addAll(sample.getDataSets())

            // fetch all datasets of the children
            foundDatasets.addAll(fetchDescendantDatasets(sample))
        }

        return foundDatasets
    }

    /**
     * Fetches descendant datstes of a sample
     * @param sample
     * @return list of descendant datasets
     */
    private static List<DataSet> fetchDescendantDatasets(final Sample sample) {
        List<DataSet> foundSets = new ArrayList<>()

        // fetch all datasets of the children recursively
        for (Sample child : sample.getChildren()) {
            final List<DataSet> foundChildrenDatasets = child.getDataSets()
            foundSets.addAll(foundChildrenDatasets)
            foundSets.addAll(fetchDescendantDatasets(child))
        }

        return foundSets
    }


}
