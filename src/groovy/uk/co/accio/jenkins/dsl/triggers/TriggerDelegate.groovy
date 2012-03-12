package uk.co.accio.jenkins.dsl.triggers

class TriggerDelegate implements Buildable {

    String topLevelElement = 'triggers'
    def triggers = []

    def void build(GroovyObject builder) {
        def obj = {
            "${topLevelElement}"(class: 'vector') {
                out << triggers
            }
        }
        obj.delegate = builder
        obj()
    }

    void scm(Closure cl) {
        cl.delegate = new ScmDelegate()
        cl.resolveStrategy = Closure.DELEGATE_FIRST
        cl()

        triggers << cl.delegate
    }

    void timer(Closure cl) {
        cl.delegate = new TimerDelegate()
        cl.resolveStrategy = Closure.DELEGATE_FIRST
        cl()

        triggers << cl.delegate
    }
}