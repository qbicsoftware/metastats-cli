package life.qbic.metastats.datamodel

class Condition {
    String label
    String value
    String sampleType

    Condition(String label, String value, String sampleType) {
        this.label = label
        this.value = value
        this.sampleType = sampleType
    }
}
