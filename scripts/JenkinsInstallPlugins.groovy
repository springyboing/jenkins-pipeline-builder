includeTargets << new File("${basedir}/scripts/_Jenkins.groovy")

jenkinsPluginList = ['grails','subversion','setenv','copyartifact','build-pipeline-plugin','jobConfigHistory','join']
jenkinsPluginExtendedList = ['violations','batch-task','gradle','groovy','extended-choice-parameter', 'AnsiColor']
jenkinsExcludePlugins = []
jenkinsIncludePlugins = []
jenkinsRestartAfterPluginInstall = true

target(default: "Installs all the required plugins on the Jenkins server") {
	depends(configureProxy, classpath)

    configureJenkinsPluginListFromBuildConfig()

    jenkinsPluginList += jenkinsIncludePlugins
    jenkinsPluginList -= jenkinsExcludePlugins

    jenkinsPluginList.each { pluginName ->
        jenkinsArgs = ['install-plugin', pluginName]
        executeJenkinsCommand()
    }

    if (jenkinsRestartAfterPluginInstall) {
        jenkinsArgs = ['safe-restart']
        executeJenkinsCommand()
    }
}

target(configureJenkinsPluginListFromBuildConfig: "Configuring Jenkins plugin include & exclude from buildconfig.groovy") {
    def includePlugins = buildConfig.jenkins.includePlugins
    if (includePlugins && includePlugins instanceof List) {
        jenkinsIncludePlugins += includePlugins
    }

    def excludePlugins = buildConfig.jenkins.excludePlugins
    if (excludePlugins && excludePlugins instanceof List) {
        jenkinsExcludePlugins += excludePlugins
    }
}
