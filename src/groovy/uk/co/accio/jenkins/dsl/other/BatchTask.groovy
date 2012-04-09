package uk.co.accio.jenkins.dsl.other

/**
 <hudson.plugins.batch__task.BatchTaskProperty>
      <tasks>
        <hudson.plugins.batch__task.BatchTask>
          <name>DoBatchTask</name>
          <script>println &apos;This is a batch task&apos;</script>
        </hudson.plugins.batch__task.BatchTask>
        <hudson.plugins.batch__task.BatchTask>
          <name>DoAnotherTask</name>
          <script>println &apos;This is another batch task&apos;</script>
        </hudson.plugins.batch__task.BatchTask>
      </tasks>
    </hudson.plugins.batch__task.BatchTaskProperty>
 */
class BatchTask implements Buildable {

    String topLevelElement = "hudson.plugins.batch__task.BatchTaskProperty"

    def tasks = []

    def void build(GroovyObject builder){
        def obj = {
            "${topLevelElement}"() {
                'tasks'([:]) {
                    for (task in tasks) {
                        out << task
                    }
                }
            }
        }
        obj.delegate = builder
        obj()
    }
}
