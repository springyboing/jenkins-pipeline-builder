import hudson.cli.CLI

grailsHome = ant.project.properties."environment.GRAILS_HOME"
includeTargets << new File ( "${grailsHome}/scripts/Init.groovy" )

target(main: "Installs all the required plugins on the Jenkins server") {
	depends(configureProxy, classpath)

	runCliCommand('http://33.33.33.10:8080', ['install-plugin', 'grails']) // Grails Plugin
    runCliCommand('http://33.33.33.10:8080', ['install-plugin', 'subversion'])
    runCliCommand('http://33.33.33.10:8080', ['install-plugin', 'git'])
    runCliCommand('http://33.33.33.10:8080', ['install-plugin', 'setenv'])
    runCliCommand('http://33.33.33.10:8080', ['install-plugin', 'copyartifact'])
    runCliCommand('http://33.33.33.10:8080', ['install-plugin', 'port-allocator'])
    runCliCommand('http://33.33.33.10:8080', ['install-plugin', 'build-pipeline-plugin'])
//    runCliCommand('http://33.33.33.10:8080', ['install-plugin', 'violations'])
    runCliCommand('http://33.33.33.10:8080', ['install-plugin', 'jobConfigHistory'])
//    runCliCommand('http://33.33.33.10:8080', ['install-plugin', 'batch-task'])
//    runCliCommand('http://33.33.33.10:8080', ['install-plugin', 'gradle'])
//    runCliCommand('http://33.33.33.10:8080', ['install-plugin', 'groovy'])
    //extended-choice-parameter 
    runCliCommand('http://33.33.33.10:8080', ['safe-restart'])
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