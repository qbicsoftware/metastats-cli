package life.qbic.metastats.io

import groovy.json.JsonSlurper

class JsonParser {

    String conf

    JsonParser(String configFile){
        conf = configFile
    }

    Map parse(){
        File file = new File(conf)
        return (Map) new JsonSlurper().parseText(file.text)
    }

}
