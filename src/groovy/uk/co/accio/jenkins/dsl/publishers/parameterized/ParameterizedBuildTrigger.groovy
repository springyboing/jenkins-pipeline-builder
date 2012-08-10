package uk.co.accio.jenkins.dsl.publishers.parameterized

class ParameterizedBuildTrigger  implements Buildable {

    String topLevelElement = "hudson.tasks.BuildTrigger"

    String childProjects

    def void build(GroovyObject builder) {
        def obj = {
            "${topLevelElement}"() {
                'childProjects'(childProjects, [:])
            }
        }
        obj.delegate = builder
        obj()
    }
}
