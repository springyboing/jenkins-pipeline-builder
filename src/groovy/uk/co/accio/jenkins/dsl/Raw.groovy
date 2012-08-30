package uk.co.accio.jenkins.dsl

class Raw implements Buildable {

    String value

    def void build(GroovyObject builder){
        def obj = {
            mkp.yieldUnescaped value
        }
        obj.delegate = builder
        obj()
    }
}
