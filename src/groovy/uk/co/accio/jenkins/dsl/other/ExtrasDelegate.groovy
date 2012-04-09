package uk.co.accio.jenkins.dsl.other

class ExtrasDelegate implements Buildable {

    def extras = []

    void batchTasks(Closure cl) {
        cl.delegate = new BatchTaskDelegate()
        cl.resolveStrategy = Closure.DELEGATE_FIRST
        cl()

        extras << cl.delegate
    }

    def void build(GroovyObject builder){
        def obj = {
            for (extra in extras) {
                out << extra
            }
        }
        obj.delegate = builder
        obj()
    }

}
