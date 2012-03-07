package uk.co.accio.jenkins

class BuilderDelegate {

    BuilderDelegate() {
    }

    void grails(Closure cl) {
        cl.delegate = new GrailsDelegate()
        cl.resolveStrategy = Closure.DELEGATE_FIRST
        cl()

        println "Grails: "
    }
    void copyArtifact(Closure cl) {
        cl.delegate = new CopyArtifactDelegate()
        cl.resolveStrategy = Closure.DELEGATE_FIRST
        cl()

        println "copyArtifact: "
    }
}