package uk.co.accio.jenkins

class BuildDelegate {

    void name(String name) {
    }

    void desc(String desc) {
    }

    void scms(Closure cl) {
        cl.delegate = new ScmDelegate()
        cl.resolveStrategy = Closure.DELEGATE_FIRST
        cl()
    }

    void triggers(Closure cl) {
        cl.delegate = new TriggerDelegate()
        cl.resolveStrategy = Closure.DELEGATE_FIRST
        cl()
    }

    void builders(Closure cl) {
        cl.delegate = new BuilderDelegate()
        cl.resolveStrategy = Closure.DELEGATE_FIRST
        cl()
    }

    void buildWrappers(Closure cl) {
        cl.delegate = new BuilderWrapperDelegate()
        cl.resolveStrategy = Closure.DELEGATE_FIRST
        cl()
    }

    void publishers(Closure cl) {
        cl.delegate = new PublisherDelegate()
        cl.resolveStrategy = Closure.DELEGATE_FIRST
        cl()
    }
}
