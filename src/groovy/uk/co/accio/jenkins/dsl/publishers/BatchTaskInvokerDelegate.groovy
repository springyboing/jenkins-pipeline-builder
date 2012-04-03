package uk.co.accio.jenkins.dsl.publishers

/**
 *
    invokeBatchTasks {
        task {
            project ''
            name ''
        }
        task {
            project ''
            name ''
        }
        threshold 'SUCCESS'
    }
 }
 */
class BatchTaskInvokerDelegate extends BatchTaskInvoker {

    void threshold(String threshold) {
        this.threshold = threshold?.toUpperCase() as Threshold
    }

    void task(Closure cl) {
        cl.delegate = new BatchTaskDelegate()
        cl.resolveStrategy = Closure.DELEGATE_FIRST
        cl()

        batchTasks << cl.delegate
    }
}

class BatchTaskDelegate extends BatchTask {

    void name(name) {
        this._name = name
    }

    void project(project) {
        this._project = project
    }

}
