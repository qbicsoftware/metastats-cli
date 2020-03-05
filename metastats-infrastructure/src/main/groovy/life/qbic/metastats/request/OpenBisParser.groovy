package life.qbic.metastats.request

import ch.ethz.sis.openbis.generic.asapi.v3.dto.experiment.Experiment
import ch.ethz.sis.openbis.generic.asapi.v3.dto.sample.Sample
import life.qbic.metastats.datamodel.MetaStatsExperiment
import life.qbic.metastats.datamodel.MetaStatsSample
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

class OpenBisParser {

    private static final Logger LOG = LogManager.getLogger(OpenBisParser.class)

    static def createMetaStatsExperiment(Experiment exp){
        String type = exp.type.code

        return new MetaStatsExperiment(type,exp.properties)
    }

    List<MetaStatsSample> getPreparationSamples(List<Sample> samples){
        List<MetaStatsSample> allSamples = []

        samples.each{sample ->
            //create sample object
            String type = sample.type.code

            if(type == "Q_TEST_SAMPLE"){
                //get parent samples
                List parents = getAllParents(sample)
                //get children samples
                List children = getAllChildren(sample)

                //children+parents are relatives of preparations sample!
                allSamples << createMetaStatsSample(parents+children, sample)
            }
            else{
                //todo is this required?
                allSamples += getPreparationSamples(sample.children)
            }
        }
        return allSamples
    }

    def createMetaStatsSample(List<Sample> relatedSamples, Sample prepSample){
        String code = prepSample.code
        String type = prepSample.code

        HashMap allProperties = prepSample.properties
        //add the code
        allProperties.put("Q_TEST_SAMPLE_CODE",code)

        relatedSamples.each {sample ->
            sample.properties.each {prop,value ->
                allProperties.put(sample.type.code+"_CODE",sample.code)
                if(prop == "Q_SECONDARY_NAME"){
                    allProperties.put("Q_SECONDARY_NAME_"+sample.type.code,value)
                }else{
                    allProperties.put(prop,value)
                }
            }
        }

        MetaStatsSample sample = new MetaStatsSample(code, type, allProperties)

        relatedSamples.each {
            sample.addRelatives(it.code)
        }

        return sample
    }

    List<Sample> getAllParents(Sample preparationSample){
        List parents = []

        preparationSample.parents.each {parent->
            //MetaStatsSample parentSample = new MetaStatsSample(parent.code, parent.type.code,parent.properties)
            parents.add(parent)

            if(parent.parents != null){//parent.parents.size() > 0 && parent.parents.get(0) instanceof Sample){
                parents += getAllParents(parent)
            }
        }

        return parents
    }

    List<Sample> getAllChildren(Sample preparationSample){
        List children = []

        preparationSample.children.each {child->
            //MetaStatsSample childSample = new MetaStatsSample(child.code, child.type.code,child.properties)
            children.add(child)

            if(child.children != null){//child.children.size() > 0 && child.children.get(0) instanceof Sample){
                children += getAllChildren(child)
            }
        }

        return children
    }
}
