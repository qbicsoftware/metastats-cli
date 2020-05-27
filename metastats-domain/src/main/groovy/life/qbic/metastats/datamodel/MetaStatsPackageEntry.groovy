package life.qbic.metastats.datamodel

class MetaStatsPackageEntry {
    String sampleName
    HashMap<String,String> properties

    MetaStatsPackageEntry(String sampleName, HashMap<String,String> properties){
        this.sampleName = sampleName
        this.properties = properties
    }
}
