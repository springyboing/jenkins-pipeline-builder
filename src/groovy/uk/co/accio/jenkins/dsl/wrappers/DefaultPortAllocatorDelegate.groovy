package uk.co.accio.jenkins.dsl.wrappers

class DefaultPortAllocatorDelegate implements Buildable {

    String topLevelElement = "org.jvnet.hudson.plugins.port__allocator.DefaultPortType"
    String name

    void name(name) {
        this.name = name
    }

    def void build(GroovyObject builder) {
        def obj = {
            "${topLevelElement}"() {
                'name'(name, [:])
            }
        }
        obj.delegate = builder
        obj()
    }
}
