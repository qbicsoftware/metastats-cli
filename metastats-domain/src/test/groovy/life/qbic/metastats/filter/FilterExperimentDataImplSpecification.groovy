package life.qbic.metastats.filter


import spock.lang.Specification

class FilterExperimentDataImplSpecification extends Specification{

    def mockedMSMetadataPackageOutput = Mock(MSMetadataPackageOutput)
    def filterExperimentData = new FilterExperimentDataImpl(mockedMSMetadataPackageOutput)

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

     def "loading schema successfully"(){
         given:
         MetaStatsMetadata m = new MetaStatsMetadata()

         when:
         def res = m.getPropertiesFromSchema()

         //def list = res.get("properties")
         //list.each{key, val -> println "$key"}

         then:
         res != null
         assert res instanceof Map
    }

}
