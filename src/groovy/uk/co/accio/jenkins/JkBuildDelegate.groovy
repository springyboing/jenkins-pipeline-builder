package uk.co.accio.jenkins

class JkBuildDelegate {

    void name(String name) {
        println "Name: " + name
    }

    void desc(String desc) {
        println "Desc: " + desc
    }

    void scms(Closure cl) {
        cl.delegate = new ScmDelegate()
        cl.resolveStrategy = Closure.DELEGATE_FIRST
        cl()

        println "Scms: "
    }

    void triggers(Closure cl) {
        cl.delegate = new TriggerDelegate()
        cl.resolveStrategy = Closure.DELEGATE_FIRST
        cl()

        println "Triggers: "
    }

    void builders(Closure cl) {
        cl.delegate = new BuilderDelegate()
        cl.resolveStrategy = Closure.DELEGATE_FIRST
        cl()

        println "Builders: "
    }

    void buildWrappers(Closure cl) {
        cl.delegate = new BuilderWrapperDelegate()
        cl.resolveStrategy = Closure.DELEGATE_FIRST
        cl()

        println "BuildWrappers: "
    }

    void publishers(Closure cl) {
        cl.delegate = new PublisherDelegate()
        cl.resolveStrategy = Closure.DELEGATE_FIRST
        cl()

        println "Publishers: "
    }
}
