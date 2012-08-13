package uk.co.accio.jenkins.dsl.publishers.parameterised.config

class CurrentBuildParameters implements Buildable {

    String topLevelElement = "hudson.plugins.parameterizedtrigger.CurrentBuildParameters"

    def void build(GroovyObject builder) {
        def obj = {
            "${topLevelElement}"([:])
        }
        obj.delegate = builder
        obj()
    }
}
