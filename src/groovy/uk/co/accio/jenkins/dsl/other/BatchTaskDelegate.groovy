package uk.co.accio.jenkins.dsl.other


class BatchTaskDelegate extends BatchTask {

    void task(Closure cl) {
        cl.delegate = new TaskDelegate()
        cl.resolveStrategy = Closure.DELEGATE_FIRST
        cl()

        tasks << cl.delegate
    }

    class Task implements Buildable {

        String _name
        String _script

        def void build(GroovyObject builder){
        def obj = {
            "task"([:]) {
                'name'(_name, [:])
                'script'(_script, [:])
            }
        }
        obj.delegate = builder
        obj()
    }
    }

    class TaskDelegate extends Task {

        void name(name) {
            this._name
        }
        void script(script) {
            this._script
        }
    }
}
