package uk.co.accio.jenkins.dsl.builders

/**
 *
 <hudson.tasks.Shell>
      <command>ls -lash</command>
    </hudson.tasks.Shell>

 shell "ls -lash"
 *
 */
class ShellDelegate implements Buildable {

    static String name = 'shell'

    String _topLevelElement = 'hudson.tasks.Shell'
    String command

    void topLevelElement(tle) {
        this._topLevelElement = tle
    }
    void command(command) {
        this.command = command
    }
    
    def void build(GroovyObject builder){
        def obj = {
            "${_topLevelElement}"([:]) {
                'command'(command, [:])

            }
        }
        obj.delegate = builder
        obj()
    }
}