package life.qbic.metastats.request

import groovy.json.JsonSlurper

class MetaStatsController {

    String conf
    String projectCode
    ProjectSpecification spec

    MetaStatsController(String configFile, String projectCode, ProjectSpecification spec){
        conf = configFile
        this.projectCode = projectCode
        this.spec = spec
    }

    def parse(){
        new JsonSlurper().parseText(new File(conf).text)
    }

    def findProject(){
        spec.requestProjectMetadata(projectCode)
    }
}
