package life.qbic.metastats.filter

import life.qbic.metastats.datamodel.MetaStatsSample
import spock.lang.Specification

class FilterExperimentDataImplSpecification extends Specification{


    def "add method extends map successfully"(){
        given:
        Map meta = new HashMap()
        meta.put("haha","hihi")

        Map props = new HashMap<String, String>()
        props.put("Q_Test","value")

        when:
        FilterExperimentDataImpl.add(meta,props)

        then:
        meta == ["Q_Test":"value","haha":"hihi"]
    }

    def "correctly transform metastats samples to package entries"(){
        given:
        Map props = new HashMap<String, String>()
        props.put("sampleName","QXXXXXX")
        props.put("species","value")
        props.put("sequencingDevice","value")

        MetaStatsSample sample1 = new MetaStatsSample("QXXXXXX","Q_TEST_SAMPLE",props)
        sample1.addRelatives("QFSVIENTITY-1")

        when:
        List res = FilterExperimentDataImpl.createMetadataPackageEntries([sample1])

        then:
        res.get(0).sampleName == "QXXXXXX"
        assert !res.get(0).properties.containsKey("sampleName")
    }

   /** def "incomplete metaStats-metadata-package gives a warning"(){
        given:
        def projectMetadata = new HashMap<String,String>()
        //todo fill

        when:
        filterExperimentData.createMetaStatsMetadataPackage(projectMetadata)

        then:
        thrown(IllegalArgumentException)
    }

    def "complete metaStats-metadata-package gives no warning"(){
        given:
        def projectMetadata = new HashMap<String,String>()
        //todo fill

        when:
        filterExperimentData.createMetaStatsMetadataPackage(projectMetadata)

        then:
        !thrown(IllegalArgumentException)
    }*/

    /** def "loading schema successfully"(){
         given:
         //MetaStatsMetadata m = new MetaStatsMetadata()

         when:
         //def res = m.getPropertiesFromSchema()

         //def list = res.get("properties")
         //list.each{key, val -> println "$key"}

         then:
         res != null
         assert res instanceof Map
    }*/

}
