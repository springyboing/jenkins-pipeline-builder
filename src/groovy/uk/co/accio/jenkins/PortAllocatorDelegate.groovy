package uk.co.accio.jenkins

/**
 *
 * "org.jvnet.hudson.plugins.port__allocator.PortAllocator" {
        ports("org.jvnet.hudson.plugins.port__allocator.DefaultPortType") {
            name 'GRAILS_HTTP_PORT'
        }
    }
 */
class PortAllocatorDelegate implements Buildable {

    String topLevelElement = 'org.jvnet.hudson.plugins.port__allocator.PortAllocator'
    def ports = []

    void ports(Closure cl) {
        def delegate = new PortAllocatorDelegate()
        ports << delegate
        cl.delegate = delegate
        cl.resolveStrategy = Closure.DELEGATE_FIRST
        cl()

        println "PortAllocator: "
    }

    def void build(GroovyObject builder) {
        def obj = {
            "${topLevelElement}"() {
            }
        }
        obj.delegate = builder
        obj()
    }

    class DefaultPortAllocatorDelegate implements Buildable {

        String topLevelElement = "org.jvnet.hudson.plugins.port__allocator.DefaultPortType"
        String name

        def void build(GroovyObject builder) {
            def obj = {
                "${topLevelElement}"() {
                    'name'(name)
                }
            }
            obj.delegate = builder
            obj()
        }
    }
}