package life.qbic.metastats.filter

import life.qbic.metastats.datamodel.MetaStatsExperiment
import life.qbic.xml.manager.StudyXMLParser
import life.qbic.xml.study.Qexperiment

import javax.xml.bind.JAXBElement
import javax.xml.bind.JAXBException

class ConditionParser {

    StudyXMLParser studyParser = new StudyXMLParser()

    ConditionParser(){

    }

    def parseProperties(MetaStatsExperiment designExperiment){

        String xmlString = designExperiment.getProperties().get("Q_EXPERIMENTAL_SETUP")

        JAXBElement<Qexperiment> expDesign
        try {
            expDesign = studyParser.parseXMLString(xmlString)

            properties.put("Q_EXPERIMENTAL_SETUP", studyParser.toString(expDesign))
            println studyParser.toString(expDesign)

            }
        catch (JAXBException e) {
            println "could not create new experimental design xml from experiment."
            e.printStackTrace()
        }
    }

}
