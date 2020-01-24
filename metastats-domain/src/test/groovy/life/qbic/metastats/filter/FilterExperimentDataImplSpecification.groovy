package life.qbic.metastats.filter

import spock.lang.Specification

class FilterExperimentDataImplSpecification extends Specification{

    def mockedMSMetadataPackageOutput = Mock(MSMetadataPackageOutput)
    def filterExperimentData = new FilterExperimentDataImpl(mockedMSMetadataPackageOutput)

    def "incomplete metaStats-metadata-package gives a warning"(){
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
    }

    /** how to test that/meaningful test?
     def "metadata only contains fields described in the schema"(){

    }
    */

}
