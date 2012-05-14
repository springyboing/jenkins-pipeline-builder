package uk.co.accio.jenkins.dsl.builders

/**
 *
 <hudson.tasks.Shell>
      <command>ls -lash</command>
    </hudson.tasks.Shell>

 shell "ls -lash"
 *
 */
class ShellDelegate extends Shell {

    static String name = 'shell'

    void topLevelElement(tle) {
        this._topLevelElement = tle
    }
    void command(command) {
        this.command = command
    }
}