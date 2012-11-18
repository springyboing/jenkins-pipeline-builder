package uk.co.accio.jenkins.dsl.publishers.parameterised.config
/**
 < hudson.plugins.parameterizedtrigger.PredefinedBuildParameters >
 < properties >
    Blur=Boo
 < /properties>
 </ hudson.plugins.parameterizedtrigger.PredefinedBuildParameters >

 */
class PredefinedBuildParameters implements Buildable {

    String topLevelElement = "hudson.plugins.parameterizedtrigger.PredefinedBuildParameters"
    String _properties

    void props(value) {
        this._properties = value
    }

    def void build(GroovyObject builder) {
        def obj = {
            "${topLevelElement}"([:]) {
                'properties'(_properties, [:])
            }
        }
        obj.delegate = builder
        obj()
    }
}
