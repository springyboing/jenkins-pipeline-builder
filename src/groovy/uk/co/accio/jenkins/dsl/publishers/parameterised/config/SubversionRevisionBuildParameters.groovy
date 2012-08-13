package uk.co.accio.jenkins.dsl.publishers.parameterised.config

class SubversionRevisionBuildParameters implements Buildable {

    String topLevelElement = "hudson.plugins.parameterizedtrigger.SubversionRevisionBuildParameters"

    def void build(GroovyObject builder) {
        def obj = {
            "${topLevelElement}"([:])
        }
        obj.delegate = builder
        obj()
    }
}
