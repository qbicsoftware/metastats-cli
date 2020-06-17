package life.qbic.metastats.filter

import life.qbic.xml.manager.StudyXMLParser
import life.qbic.xml.properties.Property
import life.qbic.xml.study.Qexperiment
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

import javax.xml.bind.JAXBElement
import javax.xml.bind.JAXBException

class ConditionParser {

    StudyXMLParser studyParser = new StudyXMLParser()
    JAXBElement<Qexperiment> expDesign

    private static final Logger LOG = LogManager.getLogger(ConditionParser.class);


    def parseProperties(Map designExperiment) {

        String xmlString = designExperiment.get("Q_EXPERIMENTAL_SETUP")
        try {
            expDesign = studyParser.parseXMLString(xmlString)
        }
        catch (JAXBException e) {
            LOG.error "could not create new experimental design xml from experiment."
            e.printStackTrace()
        }
    }

    List<Property> getSampleConditions(String sample) {
        studyParser.getFactorsAndPropertiesForSampleCode(expDesign, sample)
    }

}
