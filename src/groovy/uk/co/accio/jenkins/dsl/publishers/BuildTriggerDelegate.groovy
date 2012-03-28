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


        <name>SUCCESS</name>
        <ordinal>0</ordinal>
        <color>BLUE</color>

        <name>UNSTABLE</name>
        <ordinal>1</ordinal>
        <color>YELLOW</color>

        <name>FAILURE</name>
        <ordinal>2</ordinal>
        <color>RED</color>

 */
class BuildTriggerDelegate implements Buildable {

    String topLevelElement = "hudson.tasks.BuildTrigger"

    String childProjects
    Threshold threshold = Threshold.SUCCESS

    void childProjects(childProjects) {
        this.childProjects = childProjects
    }
    void threshold(String threshold) {
        this.threshold = threshold?.toUpperCase() as Threshold
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

enum Threshold implements Buildable {

    SUCCESS(0, 'BLUE'),
    UNSTABLE(1, 'YELLOW'),
    FAILURE(2, 'RED')

    int ordinal
    String color

    private Threshold(ordinal, color) {
        this.ordinal = ordinal
        this.color = color
    }

    def void build(GroovyObject builder) {
        def obj = {
            'threshold'([:]) {
                'name'(name(), [:])
                'ordinal'(ordinal, [:])
                'color'(color, [:])
            }
        }
        obj.delegate = builder
        obj()
    }
}
