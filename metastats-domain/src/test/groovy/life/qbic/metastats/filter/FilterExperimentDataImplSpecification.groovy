package life.qbic.metastats.filter

import ch.ethz.sis.openbis.generic.asapi.v3.IApplicationServerApi
import ch.ethz.sis.openbis.generic.asapi.v3.dto.common.search.SearchResult
import ch.ethz.sis.openbis.generic.asapi.v3.dto.experiment.fetchoptions.ExperimentFetchOptions
import ch.ethz.sis.openbis.generic.asapi.v3.dto.project.Project
import ch.ethz.sis.openbis.generic.asapi.v3.dto.project.fetchoptions.ProjectFetchOptions
import ch.ethz.sis.openbis.generic.asapi.v3.dto.project.search.ProjectSearchCriteria
import ch.ethz.sis.openbis.generic.asapi.v3.dto.sample.Sample
import ch.ethz.sis.openbis.generic.asapi.v3.dto.sample.fetchoptions.SampleFetchOptions
import ch.ethz.sis.openbis.generic.asapi.v3.dto.sample.search.SampleSearchCriteria
import ch.systemsx.cisd.common.spring.HttpInvokerUtils
import ch.systemsx.cisd.openbis.generic.shared.api.v1.dto.SampleFetchOption
import ch.systemsx.cisd.openbis.generic.shared.basic.dto.ExperimentFetchOption
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
         when:
         def res = filterExperimentData.parseSchema()

         //def list = res.get("properties")
         //list.each{key, val -> println "$key"}

         then:
         res != null
         assert res instanceof Map
    }

}
