package uk.co.accio.jenkins.dsl.builders

import uk.co.accio.jenkins.dsl.Raw

class BuilderDelegate implements Buildable {

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

    void gradle(Closure cl) {
        cl.delegate = new GradleDelegate()
        cl.resolveStrategy = Closure.DELEGATE_FIRST
        cl()

        builders << cl.delegate
    }

    void groovy(Closure cl) {
        cl.delegate = new GroovyDelegate()
        cl.resolveStrategy = Closure.DELEGATE_FIRST
        cl()

        builders << cl.delegate
    }

    void rawBuilder(String raw) {
        builders << new Raw(value: raw)
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