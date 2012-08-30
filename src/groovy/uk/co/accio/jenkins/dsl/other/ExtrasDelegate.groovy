package uk.co.accio.jenkins.dsl.other

import uk.co.accio.jenkins.dsl.other.parameterize.ParameterizeDelegate
import uk.co.accio.jenkins.dsl.Raw

class ExtrasDelegate implements Buildable {

    def extras = []
    def jdkDelegate

    void batchTasks(Closure cl) {
        cl.delegate = new BatchTaskDelegate()
        cl.resolveStrategy = Closure.DELEGATE_FIRST
        cl()

        extras << cl.delegate
    }

    void parameterize(Closure cl) {
        cl.delegate = new ParameterizeDelegate()
        cl.resolveStrategy = Closure.DELEGATE_FIRST
        cl()

        extras << cl.delegate
    }

    void jdk(Closure cl) {
        cl.delegate = new JdkDelegate()
        cl.resolveStrategy = Closure.DELEGATE_FIRST
        cl()

        jdkDelegate = cl.delegate
    }

    void rawProperties(String raw) {
        extras << new Raw(value: raw)
    }

    def void build(GroovyObject builder){
        def obj = {

            if (extras) {
                'properties'([:]) {
                    for (extra in extras) {
                        out << extra
                    }
                }
            } else {
                'properties'([:])
            }
            if (jdkDelegate) {
                out << jdkDelegate
            }
        }
        obj.delegate = builder
        obj()
    }

}
