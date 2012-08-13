package uk.co.accio.jenkins.dsl.publishers.parameterised

class BuildTrigger implements Buildable {

    String topLevelElement = "hudson.plugins.parameterizedtrigger.BuildTrigger"

    List configs = []
    String projects
    Boolean withNoParams = false
    Condition condition = Condition.SUCCESS

    def void build(GroovyObject builder) {
        def obj = {
            "${topLevelElement}"([:]) {
                'configs'([:]) {
                    'hudson.plugins.parameterizedtrigger.BuildTriggerConfig'([:]) {
                        'configs'([:]) {
                            for (config in configs) {
                                out << config
                            }
                        }
                        'projects'(projects, [:])
                        out << condition
                        'triggerWithNoParameters'(withNoParams, [:])
                    }
                }
            }
        }
        obj.delegate = builder
        obj()
    }
}
