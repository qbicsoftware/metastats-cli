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
        def res = creator.getConditions([entry,entry])

        then:
        res.sort() == ["condition: genotype","condition: treatment"].sort()
    }

    def "associate condition header correctly with sample value"(){
        given:
        List<Condition> conditions = [new Condition("genotype","mutant"),new Condition("treatment","non")]
        MetaStatsPackageEntry entry = new MetaStatsPackageEntry("this is an id", ["condition":conditions, "sampleName":"name"] as HashMap)
        TSVFileCreator creator = new TSVFileCreator()

        when:
        def res = creator.createFileContent([entry,entry,entry])

        def output = res.toString().split("\n")
        def headerRow = output[0].split("\t")

        then:
        headerRow[-1].contains("condition:")
        headerRow[-2].contains("condition:")
        !headerRow[-3].contains("condition:")
        assert res != null
    }
}
