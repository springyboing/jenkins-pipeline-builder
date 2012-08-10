package uk.co.accio.jenkins.dsl.other.parameterize

class StringParam implements Buildable {

    static String name = 'string'

    String topLevelElement = "hudson.model.StringParameterDefinition"
    String _name
    String _description
    String _defaultValue

    def void build(GroovyObject builder) {
        def obj = {
            "${topLevelElement}"([:]) {
                'name'(_name, [:])
                'description'(_description, [:])
                'defaultValue'(_defaultValue, [:])
            }
        }
        obj.delegate = builder
        obj()
    }
}

class StringParamDelegate extends StringParam {

    void name(name) {
        this._name = name
    }
    void description(value) {
        this._description = value
    }
    void defaultValue(value) {
        this._defaultValue = value
    }
}