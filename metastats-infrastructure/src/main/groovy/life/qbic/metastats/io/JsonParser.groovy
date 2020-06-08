package life.qbic.metastats.io

import groovy.json.JsonSlurper

class JsonParser {

    String path

    JsonParser(String filePath){
        path = filePath
    }

    Map parse() throws FileNotFoundException{
        File file = new File(path)
        if(file != null) return (Map) new JsonSlurper().parseText(file.text)

        throw new FileNotFoundException("File $path was not found")
    }

    Map parseStream()throws FileNotFoundException{
        InputStream stream = JsonParser.class.classLoader.getResourceAsStream(path)

        if(stream != null){
            return (Map) new JsonSlurper().parseText(stream.text)
        }
        throw new FileNotFoundException("Resource $path was not found")
    }

}
