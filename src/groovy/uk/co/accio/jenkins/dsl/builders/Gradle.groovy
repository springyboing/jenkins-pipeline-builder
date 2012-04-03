package uk.co.accio.jenkins.dsl.builders

/**
    <hudson.plugins.gradle.Gradle>
      <description>a</description>
      <switches>b</switches>
      <tasks>c</tasks>
      <rootBuildScriptDir>d</rootBuildScriptDir>
      <buildFile>e</buildFile>
      <gradleName>(Default)</gradleName>
      <useWrapper>false</useWrapper>
    </hudson.plugins.gradle.Gradle>
 */
class Gradle implements Buildable {

    String topLevelElement = "hudson.plugins.gradle.Gradle"
    String _description
    String _switches
    String _tasks
    String _rootBuildScriptDir
    String _buildFile
    String _name = 'Default'
    Boolean _useWrapper = false

    def void build(GroovyObject builder){
        def obj = {
            "${topLevelElement}"() {
                'description'(_description, [:])
                'switches'(_switches, [:])
                'tasks'(_tasks, [:])
                'rootBuildScriptDir'(_rootBuildScriptDir, [:])
                'buildFile'(_buildFile, [:])
                'gradleName'(_name, [:])
                'useWrapper'(_useWrapper, [:])
            }
        }
        obj.delegate = builder
        obj()
    }
}
