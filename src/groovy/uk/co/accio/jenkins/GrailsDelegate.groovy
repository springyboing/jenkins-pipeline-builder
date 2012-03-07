package uk.co.accio.jenkins

/**
 *
 *<builders>
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
  </builders>
 *
 */
class GrailsDelegate implements Buildable {

    GrailsDelegate(){
    }

    String topLevelElement = 'com.g2one.hudson.grails.GrailsBuilder'
    String name = '(Default)'
    String targets
    String grailsWorkDir
    String projectWorkDir
    String projectBaseDir
    String serverPort
    String props
    Boolean forceUpgrade = true
    Boolean nonInteractive = true

    void topLevelElement(tle) {
        this.topLevelElement = tle
    }
    void name(name) {
        this.name = name
    }
    void targets(targets) {
        this.targets = targets
    }
    void grailsWorkDir(grailsWorkDir){
        this.grailsWorkDir = grailsWorkDir
    }
    void projectWorkDir(projectWorkDir) {
        this.projectWorkDir = projectWorkDir
    }
    void projectBaseDir(projectBaseDir){
        this.projectBaseDir = projectBaseDir
    }
    void serverPort(serverPort) {
        this.serverPort = serverPort
    }
    void properties(props) {
        this.props = props
    }
    void forceUpgrade(Boolean forceUpgrade) {
        this.forceUpgrade = forceUpgrade
    }
    void nonInteractive(Boolean nonInteractive) {
        this.nonInteractive = nonInteractive
    }

    def void build(GroovyObject builder){
        def obj = {
            "${topLevelElement}"() {
                'targets'(targets)
                'name'(name)
                'grailsWorkDir'(grailsWorkDir)
                'projectWorkDir'(projectWorkDir)
                'projectBaseDir'(projectBaseDir)
                'serverPort'(serverPort)
                'properties'(props)
                'forceUpgrade'(forceUpgrade)
                'nonInteractive'(nonInteractive)
            }
        }
        obj.delegate = builder
        obj()
    }
}