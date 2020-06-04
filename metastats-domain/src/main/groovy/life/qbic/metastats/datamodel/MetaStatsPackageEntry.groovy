package life.qbic.metastats.datamodel

class MetaStatsPackageEntry {
    String entryId
    HashMap properties

    MetaStatsPackageEntry(String entryId, HashMap properties){
        this.entryId = entryId
        this.properties = properties
    }
}
