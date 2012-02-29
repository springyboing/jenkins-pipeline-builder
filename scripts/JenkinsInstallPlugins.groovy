import hudson.cli.CLI

grailsHome = ant.project.properties."environment.GRAILS_HOME"
includeTargets << new File ( "${grailsHome}/scripts/Init.groovy" )

target(main: "The description of the script goes here!") {
	depends(configureProxy, classpath)

	runCliCommand('localhost:8080', ['help'])
}

setDefaultTarget(main)

def runCliCommand(String rootUrl, List<String> args, InputStream input = System.in, OutputStream output = System.out, OutputStream err = System.err) {
    def CLI cli = new CLI(rootUrl.toURI().toURL())
    cli.execute(args, input, output, err)
    cli.close()
}

/*

create-job < config.xml

wget http://localhost:8080/jnlpJars/jenkins-cli.jar
java -jar jenkins-cli.jar -s http://localhost:8080 install-plugin checkstyle
java -jar jenkins-cli.jar -s http://localhost:8080 install-plugin cloverphp
java -jar jenkins-cli.jar -s http://localhost:8080 install-plugin dry
java -jar jenkins-cli.jar -s http://localhost:8080 install-plugin htmlpublisher
java -jar jenkins-cli.jar -s http://localhost:8080 install-plugin jdepend
java -jar jenkins-cli.jar -s http://localhost:8080 install-plugin plot
java -jar jenkins-cli.jar -s http://localhost:8080 install-plugin pmd
java -jar jenkins-cli.jar -s http://localhost:8080 install-plugin violations
java -jar jenkins-cli.jar -s http://localhost:8080 install-plugin xunit
java -jar jenkins-cli.jar -s http://localhost:8080 safe-restart


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