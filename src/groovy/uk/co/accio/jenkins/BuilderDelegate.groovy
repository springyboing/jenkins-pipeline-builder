package uk.co.accio.jenkins

class BuilderDelegate  implements Buildable {

    String topLevelElement = 'builder'
    def builders = []

    BuilderDelegate() {
    }

    void grails(Closure cl) {
        cl.delegate = new GrailsDelegate()
        cl.resolveStrategy = Closure.DELEGATE_FIRST
        cl()

        builders << cl.delegate
        println "Grails: "
    }
    void copyArtifact(Closure cl) {
        cl.delegate = new CopyArtifactDelegate()
        cl.resolveStrategy = Closure.DELEGATE_FIRST
        cl()

        builders << cl.delegate
        println "copyArtifact: "
    }

    def void build(GroovyObject builder) {
        def obj = {
            "${topLevelElement}" {
                out << builders
            }
        }
        obj.delegate = builder
        obj()
    }
}