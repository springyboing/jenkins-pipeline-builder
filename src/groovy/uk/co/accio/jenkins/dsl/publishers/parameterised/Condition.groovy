package uk.co.accio.jenkins.dsl.publishers.parameterised

enum Condition implements Buildable {

    SUCCESS,
    UNSTABLE,
    UNSTABLE_OR_BETTER,
    UNSTABLE_OR_WORSE,
    FAILED,
    ALWAYS

     def void build(GroovyObject builder) {
        def obj = {
            'condition'(this.name(), [:])
        }
        obj.delegate = builder
        obj()
    }
}
