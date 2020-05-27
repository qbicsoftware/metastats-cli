package life.qbic.metastats.fileCreator

import life.qbic.metastats.datamodel.MetaStatsPackageEntry

class TSVFileCreator implements FileCreator{

    private String missingValues = "NA"

    @Override
    StringBuilder createFileContent(List<MetaStatsPackageEntry> entries) {
        StringBuilder fileContent = new StringBuilder()
        //create header with keywords and search for values in the samples
        entries.each {entry ->
            fileContent << entry.sampleName
            entry.properties.each {property ->
                fileContent << "\t" + property.value
            }
            fileContent << "\n"
        }
        return fileContent
    }
}
