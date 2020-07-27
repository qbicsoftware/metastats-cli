package life.qbic.metastats.request

import ch.ethz.sis.openbis.generic.asapi.v3.dto.experiment.Experiment
import ch.ethz.sis.openbis.generic.asapi.v3.dto.sample.Sample
import life.qbic.metastats.datamodel.MetaStatsExperiment
import life.qbic.metastats.datamodel.MetaStatsSample
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

/**
 * Handles how OpenBis data is parsed
 *
 * This class is responsible for creating MetaStatsExperiments and MetaStatsSamples from OpenBis experiments and samples.
 * Based on OpenBis preparation samples it fetches the sample information of the parent and child samples. All this information
 * is condensed in one MetaStatsSample. This sample will be contain all sample information required for the output.
 * Non unique properties such as Q_SECONDARY_NAME are masked.
 * This class should be used to create MetaStats objects from OpenBis experiments and samples.
 *
 * @since: 1.0
 * @author: Jennifer Bödker
 *
 */
class OpenBisParser {

    private static final Logger LOG = LogManager.getLogger(OpenBisParser.class)

    /**
     * Creates a metastats experiment from an openbis experiment
     * @param exp from the openbis api
     * @return metastats experiment object
     */
    static def createMetaStatsExperiment(Experiment exp) {
        String type = exp.type.code
        List samples = []

        exp.samples.each { sample ->
            samples << sample.code
        }

        return new MetaStatsExperiment(type, exp.properties, samples)
    }

    /**
     * Retrieves metatstats preparation samples from a list of openbis samples by
     * iterating over all related samples of the given sample list
     * @param samples of openbis
     * @return list of metastats samples on level of preparation samples
     */
    List<MetaStatsSample> getPreparationSamples(List<Sample> samples) {
        List<MetaStatsSample> allSamples = []

        samples.each { sample ->
            //create sample object
            String type = sample.type.code

            if (type == "Q_TEST_SAMPLE") {
                //get parent samples
                List parents = getAllParents(sample)
                //get children samples
                List children = getAllChildren(sample)

                //children+parents are relatives of preparations sample!
                allSamples << createMetaStatsSample(parents + children, sample)
            } else {
                allSamples += getPreparationSamples(sample.children)
            }
        }
        return allSamples
    }

    /**
     * Creates MetaStatsSamples from openbis samples of the preparation level sample and all related samples
     * @param relatedSamples are samples that are in the openbis hierarchy below or above the given preparation samples
     * @param prepSample as an openbis sample
     * @return a MetaStatsSample created from the given sample information
     */
    static MetaStatsSample createMetaStatsSample(List<Sample> relatedSamples, Sample prepSample) {
        String code = prepSample.code
        String type = prepSample.type.code

        Map allProperties = prepSample.properties
        //add the code
        allProperties.put("Q_TEST_SAMPLE_CODE", code)

        relatedSamples.each { sample ->
            sample.properties.each { prop, value ->
                allProperties.put(sample.type.code + "_CODE", sample.code)
                if (prop == "Q_SECONDARY_NAME") {
                    allProperties.put("Q_SECONDARY_NAME_" + sample.type.code, value)
                } else {
                    allProperties.put(prop, value)
                }
            }
        }

        MetaStatsSample sample = new MetaStatsSample(code, type, allProperties)

        relatedSamples.each {
            sample.addRelatives(it.code)
        }

        return sample
    }

    /**
     * Retrieves all parent samples of a given preparation sample
     * @param preparationSample from openbis
     * @return a list of openbis samples that are hierarchically above the preparation sample
     */
    List<Sample> getAllParents(Sample preparationSample) {
        List parents = []

        preparationSample.parents.each { parent ->
            parents.add(parent)

            if (parent.parents != null) {
                parents += getAllParents(parent)
            }
        }

        return parents
    }

    /**
     * Retrieves all child samples of a given preparation sample
     * @param preparationSample from openbis
     * @return a list of openbis samples that are hierarchically below the preparation sample
     */
    List<Sample> getAllChildren(Sample preparationSample) {
        List children = []

        preparationSample.children.each { child ->
            children.add(child)

            if (child.children != null) {
                children += getAllChildren(child)
            }
        }

        return children
    }
}
