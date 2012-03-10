package uk.co.accio.jenkins.dsl.publishers

/**
 *    <hudson.tasks.BuildTrigger>
      <childProjects>BobJob-1330875179638</childProjects>
      <threshold>
        <name>SUCCESS</name>
        <ordinal>0</ordinal>
        <color>BLUE</color>
      </threshold>
    </hudson.tasks.BuildTrigger>
 */
class BuildTriggerDelegate implements Buildable {

    String topLevelElement = "hudson.tasks.BuildTrigger"

    String childProjects
    def threshold

    void childProjects(childProjects) {
        this.childProjects = childProjects
    }
    void threshold(Closure cl) {
        cl.delegate = new BuildTriggerThresholdDelegate()
        cl.resolveStrategy = Closure.DELEGATE_FIRST
        cl()

        threshold = cl.delegate
    }

    def void build(GroovyObject builder) {
        def obj = {
            "${topLevelElement}"() {
                'childProjects'(childProjects, [:])
                out << threshold
            }
        }
        obj.delegate = builder
        obj()
    }
}
