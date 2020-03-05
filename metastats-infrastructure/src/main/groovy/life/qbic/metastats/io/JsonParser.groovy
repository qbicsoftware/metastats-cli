package life.qbic.metastats.io

import groovy.json.JsonSlurper

class JsonParser {

    String conf

    JsonParser(String configFile){
        conf = configFile
    }

    Map parse(){
        InputStream stream = getClass().getClassLoader().getResourceAsStream(conf)
        (Map) new JsonSlurper().parseText(stream.text)
    }

}
