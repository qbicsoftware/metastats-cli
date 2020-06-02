package life.qbic.metastats

import life.qbic.metastats.datamodel.Condition
import life.qbic.metastats.datamodel.MetaStatsPackageEntry
import life.qbic.metastats.fileCreator.TSVFileCreator
import spock.lang.Specification

class TSVFileCreatorSpecification extends Specification{

    def "create correct header from metastats entries"(){
        given:
        List<Condition> conditions = [new Condition("genotype","mutant"),new Condition("treatment","non")]
        MetaStatsPackageEntry entry = new MetaStatsPackageEntry("this is an id", ["condition":conditions, "sampleName":"name"] as HashMap)
        TSVFileCreator creator = new TSVFileCreator()

        when:
        def res = creator.getConditions([entry])

        then:
        res.sort() == ["condition: genotype","condition: treatment"].sort()
    }

    def "associate condition header correctly with sample value"(){
        given:
        List<Condition> conditions = [new Condition("genotype","mutant"),new Condition("treatment","non")]
        MetaStatsPackageEntry entry = new MetaStatsPackageEntry("this is an id", ["condition":conditions, "sampleName":"name"] as HashMap)
        TSVFileCreator creator = new TSVFileCreator()

        when:
        def res = creator.createFileContent([entry])

        println res

        then:
        !res.contains("null")
        assert res != null
    }
}
