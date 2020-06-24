package life.qbic.metastats.io

import groovy.json.JsonSlurper

class JsonParser {

    String path

    /**
     * Creates a parser based on a given file paths
     * @param filePath is a path to the file that should be parsed
     */
    JsonParser(String filePath) {
        path = filePath
    }

    /**
     * Parses a file defined as attribute as a file
     * @return map of json file content
     * @throws FileNotFoundException
     */
    Map parse() throws FileNotFoundException {
        File file = new File(path)
        if (file != null) return (Map) new JsonSlurper().parseText(file.text)

        throw new FileNotFoundException("File $path was not found")
    }

    /**
     * Parses a file defined as attribute as a stream from classpath/from resources folder
     * @return map of json file content
     * @throws FileNotFoundException
     */
    Map parseStream() throws FileNotFoundException {
        InputStream stream = JsonParser.class.classLoader.getResourceAsStream(path)

        if (stream != null) {
            return (Map) new JsonSlurper().parseText(stream.text)
        }
        throw new FileNotFoundException("Resource $path was not found")
    }

}
