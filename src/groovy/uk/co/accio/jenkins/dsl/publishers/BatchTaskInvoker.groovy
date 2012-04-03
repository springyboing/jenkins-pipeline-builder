package uk.co.accio.jenkins.dsl.publishers

/**
 <hudson.plugins.batch__task.BatchTaskInvoker>
      <configs>
        <hudson.plugins.batch__task.BatchTaskInvoker_-Config>
          <project>JenkinsPipelineBuilder-Kickoff</project>
          <task>DoBatchTask</task>
        </hudson.plugins.batch__task.BatchTaskInvoker_-Config>
        <hudson.plugins.batch__task.BatchTaskInvoker_-Config>
          <project>JenkinsPipelineBuilder-Kickoff</project>
          <task>DoAnotherTask</task>
        </hudson.plugins.batch__task.BatchTaskInvoker_-Config>
      </configs>
      <threshold>
        <name>UNSTABLE</name>
        <ordinal>1</ordinal>
        <color>YELLOW</color>
      </threshold>
    </hudson.plugins.batch__task.BatchTaskInvoker>
 */
class BatchTaskInvoker implements Buildable {

    String topLevelElement = "hudson.plugins.batch__task.BatchTaskInvoker"

    Threshold threshold = Threshold.SUCCESS
    List batchTasks = []

    def void build(GroovyObject builder){
        def obj = {
            "${topLevelElement}"() {
                'configs'([:]) {
                    out << batchTasks
                }
                out << threshold
            }
        }
        obj.delegate = builder
        obj()
    }
}
     class BatchTask implements Buildable {

        String topLevelElement = "hudson.plugins.batch__task.BatchTaskInvoker_-Config"
        String _name
        String _project

        def void build(GroovyObject builder){
            def obj = {
                "${topLevelElement}"() {
                    'project'(_project, [:])
                    'task'(_task, [:])
                }
            }
            obj.delegate = builder
            obj()
        }
    }
