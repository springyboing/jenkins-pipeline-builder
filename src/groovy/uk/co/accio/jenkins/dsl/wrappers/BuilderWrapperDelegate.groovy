package uk.co.accio.jenkins.dsl.wrappers

class BuilderWrapperDelegate implements Buildable {

    String topLevelElement = 'buildWrappers'

    def wrappers = []

    void portAllocator(Closure cl) {
        cl.delegate = new PortAllocatorDelegate()
        cl.resolveStrategy = Closure.DELEGATE_FIRST
        cl()

        wrappers << cl.delegate
        println "PortAllocator: "
    }

    def void build(GroovyObject builder) {
        def obj = {
            "${topLevelElement}"() {
                out << wrappers
            }
        }
        obj.delegate = builder
        obj()
    }
}
