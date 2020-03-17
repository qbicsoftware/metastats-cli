package life.qbic.metastats.filter

import ch.ethz.sis.openbis.generic.asapi.v3.dto.experiment.Experiment
import life.qbic.metastats.datamodel.MetaStatsExperiment
import life.qbic.metastats.datamodel.MetaStatsSample

interface PropertiesMapper {

    //def mapExperimentProperties(MetaStatsExperiment experiment, List<MetaStatsSample> samples)
    Map mapExperimentToSample(MetaStatsExperiment experiment, MetaStatsSample sample)
    Map<String,String> mapSampleProperties(Map<String,String> openBisProperties)
}