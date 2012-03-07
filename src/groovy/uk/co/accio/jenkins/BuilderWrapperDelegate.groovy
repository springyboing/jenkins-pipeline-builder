package uk.co.accio.jenkins

class BuilderWrapperDelegate implements Buildable {

    String topLevelElement = 'buildWrappers'

    void portAllocator(Closure cl) {
        cl.delegate = new PortAllocatorDelegate()
        cl.resolveStrategy = Closure.DELEGATE_FIRST
        cl()

        println "PortAllocator: "
    }



    def void build(GroovyObject builder) {
        def obj = {
            "${topLevelElement}"() {
                'name'("TODO")
            }
        }
        obj.delegate = builder
        obj()
    }
}
