package uk.co.accio.jenkins.dsl.wrappers

/**
 *
 * "org.jvnet.hudson.plugins.port__allocator.PortAllocator" {
        ports("org.jvnet.hudson.plugins.port__allocator.DefaultPortType") {
            name 'GRAILS_HTTP_PORT'
        }
    }
 */
class PortAllocatorDelegate implements Buildable {

    static String name = 'portAllocator'

    String topLevelElement = 'org.jvnet.hudson.plugins.port__allocator.PortAllocator'
    def ports = []

    void ports(Closure cl) {
        def delegate = new DefaultPortAllocatorDelegate()
        ports << delegate
        cl.delegate = delegate
        cl.resolveStrategy = Closure.DELEGATE_FIRST
        cl()
    }

    def void build(GroovyObject builder) {
        def obj = {
                "${topLevelElement}"() {
                    'ports'([:]) {
                        out << ports
                    }
                }
        }
        obj.delegate = builder
        obj()
    }
}