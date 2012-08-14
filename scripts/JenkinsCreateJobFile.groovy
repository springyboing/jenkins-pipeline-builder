includeTargets << grailsScript('_GrailsPackage')

target(default: "Creates new Jenkins Job config file") {
    depends(classpath, compile)

    // These should be got form the BuildConf.groovy file or your Settings.groovy file
    def jobName = "${grailsAppName}-"
    jobName += new Date().time

    println "ArgsMap: " + argsMap
    println "ArgsMap.params: " + argsMap.params

    // When loaded from DSL I can have multiple jobs, each with there own name
    def jobs = [:]

    if (argsMap.file && new File(argsMap.file).exists()) {

        if (argsMap.file.endsWith('.groovy')) {
            
            jobs = loadJobsFromGroovyDslFile(argsMap.file)
        } else {
            jobs.put(jobName, new FileInputStream(argsMap.file))
        }

    } else if (argsMap.file && !new File(argsMap.file).exists()) {

            event "StatusError",  ['File Not Found: ' + argsMap.file]
            exit 1
    }
    
    jobs.each { name, config ->

        event "StatusUpdate", ["Creating ${name} job now."]

        def destDir = new File(argsMap.destDir ?: (buildConfig.jenkins.jobs.dir ? buildConfig.jenkins.jobs.dir : './target/jobs'))
        destDir.mkdirs()
        new File(destDir, "${name}.xml").write(config.text)

        event "StatusUpdate", ["Jenkins job config file created complete"]
    }
}

Map loadJobsFromGroovyDslFile (String dslFile ) {

    Class bcClass = classLoader.loadClass('uk.co.accio.jenkins.dsl.BuildConfigurator')
    def buildConfigurator = bcClass.newInstance()
    buildConfigurator.bindVariable("appName", grailsAppName)
    buildConfigurator.runJenkinsBuilder(new File(dslFile))

    List buildJobs = buildConfigurator.buildConfig.buildJobs
	Map buildJobMap = [:]
    if (buildJobs && !buildJobs.isEmpty()) {
	
		println "BuildJobs: " + buildJobs

		for(buildJob in buildJobs) {
			buildJobMap.put(buildJob.name, new ByteArrayInputStream(buildJob.toBuildConfigXml().getBytes()))
		}
    }
	return buildJobMap
}