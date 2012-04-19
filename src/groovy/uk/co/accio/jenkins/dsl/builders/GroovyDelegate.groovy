package uk.co.accio.jenkins.dsl.builders

/**
 groovy {
    file '' // or script ''
    name ''
    parameters ''
    scriptParameters ''
    properties ''
    javaOpts ''
    classPath ''
 }
 */
class GroovyDelegate extends Groovy {

    static String name = 'groovy'

    void file(file) {
        this._file = file
    }
    void script(script) {
        this._script = script
    }
    void name(name) {
        this._name = name
    }
    void parameters(parameters) {
        this._parameters = parameters
    }
    void scriptParameters(scriptParameters) {
        this._scriptParameters = scriptParameters
    }
    void properties(properties) {
        this._properties = properties
    }
    void javaOpts(javaOpts) {
        this._javaOpts = javaOpts
    }
    void classPath(classPath) {
        this._classPath = classPath
    }
}
