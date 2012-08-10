package uk.co.accio.jenkins.dsl.other.parameterize

class ListSubversionTagsParam implements Buildable {

    static String name = 'svnTags'

    String topLevelElement = "hudson.scm.listtagsparameter.ListSubversionTagsParameterDefinition"
    String _name
    String _description
    String _tagsDir
    String _tagsFilter
    Boolean _reverseByDate = false
    Boolean _reverseByName = false
    String _maxTags
    String _defaultValue

    def void build(GroovyObject builder) {
        def obj = {
            "${topLevelElement}"([:]) {
                'name'(_name, [:])
                'description'(_description, [:])
                'tagsDir'(_tagsDir, [:])
                'tagsFilter'(_tagsFilter, [:])
                'reverseByDate'(_reverseByDate, [:])
                'reverseByName'(_reverseByName, [:])
                'defaultValue'(_defaultValue, [:])
                'maxTags'(_maxTags, [:])
            }
        }
        obj.delegate = builder
        obj()
    }
}

class ListSubversionTagsParamDelegate extends ListSubversionTagsParam {

    void name(name) {
        this._name = name
    }

    void description(value) {
        this._description = value
    }
    void tagsDir(value) {
        this._tagsDir = value
    }
    void tagsFilter(value) {
        this._tagsFilter = value
    }
    void reverseByDate(value) {
        this._reverseByDate = value
    }
    void reverseByName(value) {
        this._reverseByName = value
    }
    void defaultValue(value) {
        this._defaultValue = value
    }
    void maxTags(value) {
        this._maxTags = value
    }
}