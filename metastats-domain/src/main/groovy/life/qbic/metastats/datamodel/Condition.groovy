package life.qbic.metastats.datamodel

/**
 * Describes condition objects
 *
 * This POJO enables to associate a condition label with a condition value
 *
 * @since: 1.0
 * @author: Jennifer Bödker
 *
 */
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
