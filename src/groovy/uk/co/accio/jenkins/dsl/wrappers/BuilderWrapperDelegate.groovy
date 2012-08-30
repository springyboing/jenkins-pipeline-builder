package uk.co.accio.jenkins.dsl.wrappers

import uk.co.accio.jenkins.dsl.Raw

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

    void raw(String value) {
        wrappers << new Raw(value: value)
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
