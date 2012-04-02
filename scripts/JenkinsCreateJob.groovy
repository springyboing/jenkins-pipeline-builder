//includeTargets << grailsScript("_GrailsClasspath")
includeTargets << grailsScript('_GrailsPackage')
includeTargets << new File("${basedir}/scripts/_Jenkins.groovy")

target(default: "Creates new Job on your Jenkins server") {
    depends(classpath, compile)

    def configXml = """\
            <?xml version='1.0' encoding='UTF-8'?>
            <project>
              <actions/>
              <keepDependencies>false</keepDependencies>
              <properties/>
              <scm class="hudson.scm.NullSCM"/>
              <canRoam>false</canRoam>
              <disabled>false</disabled>
              <blockBuildWhenDownstreamBuilding>false</blockBuildWhenDownstreamBuilding>
              <blockBuildWhenUpstreamBuilding>false</blockBuildWhenUpstreamBuilding>
              <triggers class="vector"/>
              <concurrentBuild>false</concurrentBuild>
              <builders/>
              <publishers/>
              <buildWrappers/>
            </project>""".stripIndent()

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

            if (argsMap.name) {
                jobName = argsMap.name + '-' + new Date().time
            }

            jobs.put(jobName, new FileInputStream(argsMap.file))
        }

    } else if (argsMap.file && !new File(argsMap.file).exists()) {

            grailsConsole.error 'File Not Found: ' + argsMap.file
            exit 1
    } else {
        
        jobName = 'TestJob-' + new Date().time
        jobs.put(jobName, new ByteArrayInputStream(configXml.getBytes()))
    }
    
    jobs.each { name, config ->

        grailsConsole.updateStatus "Configuring ${name} job now."

        jenkinsArgs = ['create-job', name]
        jenkinsInputStream = config
        jenkinsErrorStream =  new ByteArrayOutputStream()
        executeJenkinsCommand()

        def errorResponse = jenkinsErrorStream.toString()
        if (errorResponse.contains("already exists")) {

           grailsConsole.updateStatus "Job '${name}' already exixts.  Retry but this time update the job!"

            def jenkinsUpdateOnExists = true

            if (jenkinsUpdateOnExists) {
                jenkinsArgs = ['update-job', name]
                jenkinsInputStream = config
                jenkinsErrorStream = System.out
                executeJenkinsCommand()
            }
        }

        grailsConsole.updateStatus "Jenkins create/update complete"
    }
}

Map loadJobsFromGroovyDslFile (String dslFile ) {

    Class bcClass = classLoader.loadClass('uk.co.accio.jenkins.dsl.BuildConfigurator')
    def buildConfigurator = bcClass.newInstance()
    buildConfigurator.bindVariable("appName", grailsAppName)
    buildConfigurator.runJenkinsBuilder(new File(dslFile))

    List buildJobs = buildConfigurator.buildConfig.buildJobs
    if (buildJobs && !buildJobs.isEmpty()) {
        return buildJobs.collectEntries {
            // TODO - Don't like the ByteArrayInputStream...  Why not leave as string until passed to jenkins?
            [it.name, new ByteArrayInputStream(it.toBuildConfigXml().getBytes())]
        }
    } else {
        return [:]
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