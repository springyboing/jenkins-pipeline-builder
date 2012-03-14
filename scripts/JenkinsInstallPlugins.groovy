import hudson.cli.CLI

grailsHome = ant.project.properties."environment.GRAILS_HOME"
includeTargets << new File ( "${grailsHome}/scripts/Init.groovy" )

target(main: "Installs all the required plugins on the Jenkins server") {
	depends(configureProxy, classpath)

    def host = '192.168.1.68'
    def port = '8080'
    def jenkinsUrl = "http://${host}:${port}"

	runCliCommand(jenkinsUrl, ['install-plugin', 'grails']) // Grails Plugin
    runCliCommand(jenkinsUrl, ['install-plugin', 'subversion'])
    runCliCommand(jenkinsUrl, ['install-plugin', 'git'])
    runCliCommand(jenkinsUrl, ['install-plugin', 'setenv'])
    runCliCommand(jenkinsUrl, ['install-plugin', 'copyartifact'])
    runCliCommand(jenkinsUrl, ['install-plugin', 'port-allocator'])
    runCliCommand(jenkinsUrl, ['install-plugin', 'build-pipeline-plugin'])
//    runCliCommand(jenkinsUrl, ['install-plugin', 'violations'])
    runCliCommand(jenkinsUrl, ['install-plugin', 'jobConfigHistory'])
//    runCliCommand(jenkinsUrl, ['install-plugin', 'batch-task'])
//    runCliCommand(jenkinsUrl, ['install-plugin', 'gradle'])
//    runCliCommand(jenkinsUrl, ['install-plugin', 'groovy'])
    //extended-choice-parameter 
    runCliCommand(jenkinsUrl, ['safe-restart'])
}

setDefaultTarget(main)

def runCliCommand(String rootUrl, List<String> args, InputStream input = System.in, OutputStream output = System.out, OutputStream err = System.err) {
    def CLI cli = new CLI(rootUrl.toURI().toURL())
    cli.execute(args, input, output, err)
    cli.close()
}

/*

create-job < config.xml
https://wiki.jenkins-ci.org/display/JENKINS/Jenkins+Script+Console


  <project>-kickoff
	|		\				\				\			\
  <project>-unit-tests	  <project>-integration-tests	  <project>-funtional-tests	  <project>-custom-tests	<project>-code-review
	|		/				/				/			/
  <project>-war
	|
  <project>-release
	|		\
  <project>-deploy	  <project>-custom

*/