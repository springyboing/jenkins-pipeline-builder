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
class BuildTriggerThresholdDelegate implements Buildable {

    String topLevelElement = "threshold"

    String name
    String ordinal
    String color

    void name(name) {
        this.name = name
    }
    void ordinal(ordinal) {
        this.ordinal = ordinal
    }
    void color(color) {
        this.color = color
    }

    def void build(GroovyObject builder) {
        def obj = {
            "${topLevelElement}"() {
                'name'(name, [:])
                'ordinal'(ordinal, [:])
                'color'(color, [:])
            }
        }
        obj.delegate = builder
        obj()
    }
}
