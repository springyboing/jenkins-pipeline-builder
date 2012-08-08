includeTargets << new File("${pluginSettings.getPluginInfoForName('jenkins-pipeline-builder').getPluginDir().getPath()}/scripts/_Jenkins.groovy")

target(default: "Execute Command Against Jenkins Server") {
    depends(parseArguments, classpath)

    println "ArgsMap: " + argsMap
    println "ArgsMap.params: " + argsMap.params

    if (argsMap.params) {
        jenkinsArgs = argsMap.params

        println "file: " + argsMap.file

    } else {
        jenkinsArgs = ['help']
    }

    executeJenkinsCommand()
}
