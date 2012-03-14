package uk.co.accio.jenkins.dsl.builders

import uk.co.accio.jenkins.dsl.builders.GrailsDelegate
import uk.co.accio.jenkins.dsl.builders.CopyArtifactDelegate

class BuilderDelegate  implements Buildable {

    String topLevelElement = 'builders'
    def builders = []

    void grails(Closure cl) {
        cl.delegate = new GrailsDelegate()
        cl.resolveStrategy = Closure.DELEGATE_FIRST
        cl()

        builders << cl.delegate
    }

    void copyArtifact(Closure cl) {
        cl.delegate = new CopyArtifactDelegate()
        cl.resolveStrategy = Closure.DELEGATE_FIRST
        cl()

        builders << cl.delegate
    }

    void shell(Closure cl) {
        cl.delegate = new ShellDelegate()
        cl.resolveStrategy = Closure.DELEGATE_FIRST
        cl()

        builders << cl.delegate
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