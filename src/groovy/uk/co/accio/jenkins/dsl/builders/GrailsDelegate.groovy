package uk.co.accio.jenkins.dsl.builders

/**
 *
 *<dsl.builders>
    <com.g2one.hudson.grails.GrailsBuilder>
      <targets>test-app unit:</targets>
      <name>(Default)</name>
      <grailsWorkDir></grailsWorkDir>
      <projectWorkDir></projectWorkDir>
      <projectBaseDir></projectBaseDir>
      <serverPort>$GRAILS_HTTP_PORT</serverPort>
      <properties></properties>
      <forceUpgrade>false</forceUpgrade>
      <nonInteractive>true</nonInteractive>
    </com.g2one.hudson.grails.GrailsBuilder>
  </dsl.builders>
 *
 */
class GrailsDelegate implements Buildable {

    String _topLevelElement = 'com.g2one.hudson.grails.GrailsBuilder'
    String _name = '(Default)'
    String _targets
    String _grailsWorkDir
    String _projectWorkDir
    String _projectBaseDir
    String _serverPort
    String _properties
    Boolean _forceUpgrade = true
    Boolean _nonInteractive = true

    void topLevelElement(tle) {
        this._topLevelElement = tle
    }
    void name(name) {
        this._name = name
    }
    void targets(targets) {
        this._targets = targets
    }
    void grailsWorkDir(String grailsWorkDir) {
        this._grailsWorkDir = grailsWorkDir
    }
    void projectWorkDir(projectWorkDir) {
        this._projectWorkDir = projectWorkDir
    }
    void projectBaseDir(projectBaseDir){
        this._projectBaseDir = projectBaseDir
    }
    void serverPort(serverPort) {
        this._serverPort = serverPort
    }
    void properties(props) {
        this._properties = props
    }
    void forceUpgrade(Boolean forceUpgrade) {
        this._forceUpgrade = forceUpgrade
    }
    void nonInteractive(Boolean nonInteractive) {
        this._nonInteractive = nonInteractive
    }

    def void build(GroovyObject builder){
        def obj = {
            "${_topLevelElement}"([:]) {
                'targets'(_targets, [:])
                'name'(_name, [:])
                'grailsWorkDir'(_grailsWorkDir, [:])
                'projectWorkDir'(_projectWorkDir, [:])
                'projectBaseDir'(_projectBaseDir, [:])
                'serverPort'(_serverPort, [:])
                'properties'(_properties, [:])
                'forceUpgrade'(_forceUpgrade, [:])
                'nonInteractive'(_nonInteractive, [:])
            }
        }
        obj.delegate = builder
        obj()
    }
}