package life.qbic.metastats.fileCreator

import life.qbic.metastats.datamodel.Condition
import life.qbic.metastats.datamodel.MetaStatsPackageEntry

/**
 * Defines the TSV-file outputs
 *
 * This class is an implementation of the FileOutputCreator. It formats the file content of TSV files based on the given
 * MetaStatsEntries
 *
 * @since: 1.0
 * @author: Jennifer Bödker
 *
 */
class TsvFileOutputCreator extends FileOutputCreator {

    private static String missingValues = "NA"
    private static String fileEnding = "tsv"
    private ArrayList<String> columnOrder = ["QBiC.Code", "SampleName", "SequencingFacilityId", "SequencingDevice", "SequencingMode",
                                             "Individual", "Species", "ExtractCode", "Sex", "Tissue",
                                             "Analyte", "IntegrityNumber", "Filename"]

    /**
     * Creates the output for a given project code
     * @param projectCode
     */
    TsvFileOutputCreator(String projectCode) {
        super(fileEnding, projectCode)
    }

    @Override
    StringBuilder createFileContent(List<MetaStatsPackageEntry> entries) {
        StringBuilder fileContent = new StringBuilder()

        //add different conditions
        int pos = columnOrder.size() - 1
        columnOrder.addAll(pos, getConditions(entries))
        columnOrder.each { header ->
            fileContent << header + "\t"
        }

        fileContent.deleteCharAt(fileContent.length() - 1)
        fileContent << "\n"

        //create header with keywords and search for values in the samples
        entries.each { entry ->
            //fileContent << entry.entryId
            columnOrder.each { header ->
                String cellValue = entry.entryProperties.get(header).toString()

                if (header.contains("Condition")) {
                    String conditionLabel = header.split(":")[1].trim()
                    entry.entryProperties.get("Condition").each { Condition cond ->
                        if (cond.label == conditionLabel) cellValue = cond.value
                    }
                }
                if (cellValue == "null" || cellValue == "") cellValue = missingValues

                fileContent << cellValue + "\t"
            }
            fileContent.deleteCharAt(fileContent.length() - 1)
            fileContent << "\n"
        }
        return fileContent
    }

    /**
     * Returns all condition labels to use for the header
     * @param entries all entries containing the condition information as properties
     * @return a list of all conditions labels
     */
    List<String> getConditions(List<MetaStatsPackageEntry> entries) {
        List conditionTypes = []

        entries.each { entry ->
            entry.entryProperties.each { prop ->
                if (prop.key == "Condition") {
                    prop.value.each { Condition condition ->
                        String headerValue = "Condition: " + condition.label
                        if (!conditionTypes.contains(headerValue)) conditionTypes << headerValue
                    }
                }
            }
        }

        return conditionTypes
    }

}
