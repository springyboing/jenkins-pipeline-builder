package uk.co.accio.jenkins.dsl.other.parameterize

class ParameterizeDelegate extends Parameterize {

    void string(Closure cl) {
        cl.delegate = new StringParamDelegate()
        cl.resolveStrategy = Closure.DELEGATE_FIRST
        cl()

        params << cl.delegate
    }

    void svnTags(Closure cl) {
        cl.delegate = new ListSubversionTagsParamDelegate()
        cl.resolveStrategy = Closure.DELEGATE_FIRST
        cl()

        params << cl.delegate
    }
}

class Parameterize implements Buildable {

    String topLevelElement = "hudson.model.ParametersDefinitionProperty"

    def params = []

    def void build(GroovyObject builder){
        def obj = {
            "${topLevelElement}"() {
                'parameterDefinitions'([:]) {
                    for (param in params) {
                        out << param
                    }
                }
            }
        }
        obj.delegate = builder
        obj()
    }
}
