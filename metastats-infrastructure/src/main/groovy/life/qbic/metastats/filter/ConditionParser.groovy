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

    /**
     * Parses the experimental conditions from the experimental properties map
     * @param designExperiment map describing experiments containing the experimental setup
     */
    void parseProperties(Map designExperiment) {

        String xmlString = designExperiment.get("Q_EXPERIMENTAL_SETUP")
        try {
            expDesign = studyParser.parseXMLString(xmlString)
        }
        catch (JAXBException e) {
            LOG.error "could not create new experimental design xml from experiment."
            e.printStackTrace()
        }
    }

    /**
     * Returns the conditions for a given sample from the experimental design
     * @param sampleCode defiend by its code
     * @return list of property conditions of the sample
     */
    List<Property> getSampleConditions(String sampleCode) {
        studyParser.getFactorsAndPropertiesForSampleCode(expDesign, sampleCode)
    }

}
