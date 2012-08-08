includeTargets << new File("${pluginSettings.getPluginInfoForName('jenkins-pipeline-builder').getPluginDir().getPath()}/scripts/_Jenkins.groovy")

target(default: "Creates a build pipeline for the current Grails application") {

    println "ArgsMap: " + argsMap
    println "ArgsMap.params: " + argsMap.params

    // Blank equals default which is application name...
    promptForJobNamePrefix()

    // if (I can't find locally) {
        promptForVersionControlUrl()
    //}

    // Create DSL file...

    // Ask to create on jenkins server...
}

promptForVersionControlUrl = {
    if (!argsMap["params"]) {
        argsMap["params"] << grailsConsole.userInput("Version control url not specified. Please enter:")
    }
}

promptForJobNamePrefix = {
    if (!argsMap["params"]) {
        argsMap["params"] << grailsConsole.userInput("Job name prefix not specified. Please enter:")
    }
}

/*

create-job < config.xml

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