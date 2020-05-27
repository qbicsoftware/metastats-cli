package life.qbic.metastats.io

import groovy.json.JsonSlurper

class JsonParser {

    String path

    JsonParser(String filePath){
        path = filePath
    }

    Map parse(){
        File file = new File(path)
        return (Map) new JsonSlurper().parseText(file.text)
    }

    Map parseStream(){
        InputStream stream = JsonParser.class.classLoader.getResourceAsStream(path)
        return (Map) new JsonSlurper().parseText(stream.text)
    }

}
