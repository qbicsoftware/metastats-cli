package life.qbic.metastats.request

import groovy.json.JsonSlurper

class MetaStatsController {

    String conf
    String project

    MetaStatsController(String configFile, String projectCode){
        conf = configFile
        project = projectCode
    }

    def parse(){
        new JsonSlurper().parseText(new File(conf).text)
    }
}
