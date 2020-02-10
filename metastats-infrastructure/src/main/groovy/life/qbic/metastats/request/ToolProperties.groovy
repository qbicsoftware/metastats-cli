package life.qbic.metastats.request

import groovy.json.JsonSlurper

class ToolProperties {

    String conf

    ToolProperties(String configFile){
        conf = configFile
    }

    def parse(){
        new JsonSlurper().parseText(new File(conf).text)
    }

}
