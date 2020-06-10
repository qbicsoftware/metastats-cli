package life.qbic.metastats.fileCreator

import life.qbic.metastats.datamodel.Condition
import life.qbic.metastats.datamodel.MetaStatsPackageEntry

class TsvFileOutput extends FileOutput {

    private static String missingValues = "NA"
    private static String fileEnding = "tsv"
    private ArrayList<String> order = ["QBiC.Code","SampleName", "SequencingFacilityId", "SequencingDevice",
                                       "Individual", "Species", "ExtractCode", "Sex", "Tissue",
                                       "Analyte", "IntegrityNumber", "Filename"]

    TsvFileOutput(String projectCode){
        super(fileEnding,projectCode)
    }

    @Override
    StringBuilder createFileContent(List<MetaStatsPackageEntry> entries) {
        StringBuilder fileContent = new StringBuilder()

        //add different conditions
        int pos = order.size()-1
        order.addAll(pos, getConditions(entries))
        order.each { header ->
            fileContent << header + "\t"
        }

        fileContent.deleteCharAt(fileContent.length() - 1)
        fileContent << "\n"

        //todo sort the properties for the respective samples
        //create header with keywords and search for values in the samples
        entries.each { entry ->
            //fileContent << entry.entryId
            order.each { header ->
                String cellValue = entry.properties.get(header).toString()

                if (header.contains("Condition")) {
                    String conditionLabel = header.split(":")[1].trim()
                    entry.properties.get("Condition").each { Condition cond ->
                        if (cond.label == conditionLabel) cellValue = cond.value
                    }
                }
                if (cellValue == null || cellValue == "") cellValue = missingValues

                fileContent << cellValue + "\t"
            }
            fileContent.deleteCharAt(fileContent.length() - 1)
            fileContent << "\n"
        }
        return fileContent
    }

    List<String> getConditions(List<MetaStatsPackageEntry> entries) {
        List conditionTypes = []

        entries.each { entry ->
            entry.properties.each { prop ->
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
