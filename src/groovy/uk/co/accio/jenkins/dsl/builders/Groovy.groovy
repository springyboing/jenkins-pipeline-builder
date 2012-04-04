package uk.co.accio.jenkins.dsl.builders

/**
    <hudson.plugins.groovy.Groovy>
      <scriptSource class="hudson.plugins.groovy.FileScriptSource">
        <scriptFile>path/to/file.groovy</scriptFile>
      </scriptSource>
      <groovyName>(Default)</groovyName>
      <parameters>A</parameters>
      <scriptParameters>C</scriptParameters>
      <properties>D</properties>
      <javaOpts>E</javaOpts>
      <classPath>B</classPath>
    </hudson.plugins.groovy.Groovy>
    <hudson.plugins.groovy.Groovy>
      <scriptSource class="hudson.plugins.groovy.StringScriptSource">
        <command>println &quot;Hello World&quot;</command>
      </scriptSource>
      <groovyName>(Default)</groovyName>
      <parameters>A</parameters>
      <scriptParameters>C</scriptParameters>
      <properties>D</properties>
      <javaOpts></javaOpts>
      <classPath>B</classPath>
    </hudson.plugins.groovy.Groovy>
 */
class Groovy implements Buildable {

    String topLevelElement = "hudson.plugins.groovy.Groovy"
    String _name
    String _parameters
    String _scriptParameters
    String _properties
    String _javaOpts
    String _classPath
    String _file
    String _script

    def void build(GroovyObject builder){
        def obj = {
            "${topLevelElement}"() {

                if (_file) {
                    'scriptSource'([class: "hudson.plugins.groovy.FileScriptSource"]) {
                        'scriptFile'(_file, [:])
                    }
                } else {
                    'scriptSource'([class: "hudson.plugins.groovy.StringScriptSource"]) {
                        'command'(_script, [:])
                    }
                }
                'groovyName'(_name, [:])
                'parameters'(_parameters, [:])
                'scriptParameters'(_scriptParameters, [:])
                'properties'(_properties, [:])
                'javaOpts'(_javaOpts, [:])
                'classPath'(_classPath, [:])
            }
        }
        obj.delegate = builder
        obj()
    }
}
