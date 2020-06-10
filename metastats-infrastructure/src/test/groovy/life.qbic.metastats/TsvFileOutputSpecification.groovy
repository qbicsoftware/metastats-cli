package life.qbic.metastats

import life.qbic.metastats.datamodel.Condition
import life.qbic.metastats.datamodel.MetaStatsPackageEntry
import life.qbic.metastats.fileCreator.TsvFileOutput
import spock.lang.Specification

class TsvFileOutputSpecification extends Specification {

    def "create correct header from metastats entries"() {
        given:
        List<Condition> conditions = [new Condition("genotype", "mutant"), new Condition("treatment", "non")]
        MetaStatsPackageEntry entry = new MetaStatsPackageEntry("this is an id", ["Condition": conditions, "SampleName": "name"] as HashMap)
        TsvFileOutput creator = new TsvFileOutput()

        when:
        def res = creator.getConditions([entry, entry])

        then:
        res.sort() == ["Condition: genotype", "Condition: treatment"].sort()
    }

    def "associate condition header correctly with sample value"() {
        given:
        List<Condition> conditions = [new Condition("genotype", "mutant"), new Condition("treatment", "non")]
        MetaStatsPackageEntry entry = new MetaStatsPackageEntry("this is an id", ["Condition": conditions, "SampleName": "name"] as HashMap)
        TsvFileOutput creator = new TsvFileOutput()

        when:
        def res = creator.createFileContent([entry, entry, entry])

        def output = res.toString().split("\n")
        def headerRow = output[0].split("\t")

        then:
        headerRow[-1].contains("Condition:")
        headerRow[-2].contains("Condition:")
        !headerRow[-3].contains("Condition:")
        assert res != null
    }
}
