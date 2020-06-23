package life.qbic.metastats.datamodel

class Condition {
    String label
    String value

    /**
     * Creates a Condition which is described by a label, value
     * @param label defines the condition e.g genotype
     * @param value defines the value of the condition e.g. mutant
     */
    Condition(String label, String value) {
        this.label = label
        this.value = value
    }
}
