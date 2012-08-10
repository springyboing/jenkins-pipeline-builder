package uk.co.accio.jenkins.dsl.other

class Jdk implements Buildable {

    static String name = 'jdk'

    String _value

    def void build(GroovyObject builder){
        def obj = {
            "jdk"([:], _value)
        }
        obj.delegate = builder
        obj()
    }
}
