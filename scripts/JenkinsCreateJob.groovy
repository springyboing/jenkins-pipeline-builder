import hudson.cli.CLI
import uk.co.accio.jenkins.dsl.BuildConfigurator

includeTargets << grailsScript("_GrailsClasspath")
includeTargets << grailsScript('_GrailsPackage')
includeTargets << new File("${basedir}/scripts/_Jenkins.groovy")

target(default: "Creates new Job on your Jenkins server") {
    depends(classpath, compile)

//    def configXml = """\
//<?xml version='1.0' encoding='UTF-8'?>
//<project>
//  <actions/>
//  <keepDependencies>false</keepDependencies>
//  <properties/>
//  <scm class="hudson.scm.NullSCM"/>
//  <canRoam>false</canRoam>
//  <disabled>false</disabled>
//  <blockBuildWhenDownstreamBuilding>false</blockBuildWhenDownstreamBuilding>
//  <blockBuildWhenUpstreamBuilding>false</blockBuildWhenUpstreamBuilding>
//  <triggers class="vector"/>
//  <concurrentBuild>false</concurrentBuild>
//  <builders/>
//  <publishers/>
//  <buildWrappers/>
//</project>"""

def configXml = """\
    <?xml version='1.0' encoding='UTF-8'?>
    <project>
      <actions/>
      <keepDependencies>false</keepDependencies>
      <properties/>
      <scm class="hudson.plugins.git.GitSCM">
        <configVersion>2</configVersion>
        <userRemoteConfigs>
          <hudson.plugins.git.UserRemoteConfig>
            <name/>
            <refspec/>
            <url>git://github.com/springyboing/jenkins-pipeline-builder.git</url>
          </hudson.plugins.git.UserRemoteConfig>
        </userRemoteConfigs>
        <branches>
          <hudson.plugins.git.BranchSpec>
            <name>**</name>
          </hudson.plugins.git.BranchSpec>
        </branches>
        <disableSubmodules>false</disableSubmodules>
        <recursiveSubmodules>false</recursiveSubmodules>
        <doGenerateSubmoduleConfigurations>false</doGenerateSubmoduleConfigurations>
        <authorOrCommitter>false</authorOrCommitter>
        <clean>false</clean>
        <wipeOutWorkspace>false</wipeOutWorkspace>
        <pruneBranches>false</pruneBranches>
        <remotePoll>false</remotePoll>
        <buildChooser class="hudson.plugins.git.util.DefaultBuildChooser"/>
        <gitTool>Default</gitTool>
        <submoduleCfg class="list"/>
        <relativeTargetDir/>
        <reference/>
        <excludedRegions/>
        <excludedUsers/>
        <gitConfigName/>
        <gitConfigEmail/>
        <skipTag>false</skipTag>
        <includedRegions/>
        <scmName/>
      </scm>
      <canRoam>false</canRoam>
      <disabled>false</disabled>
      <blockBuildWhenDownstreamBuilding>false</blockBuildWhenDownstreamBuilding>
      <blockBuildWhenUpstreamBuilding>false</blockBuildWhenUpstreamBuilding>
      <triggers class="vector">
        <hudson.triggers.TimerTrigger>
          <spec>5 * * * *</spec>
        </hudson.triggers.TimerTrigger>
        <hudson.triggers.SCMTrigger>
          <spec>5 * * * *</spec>
        </hudson.triggers.SCMTrigger>
      </triggers>
      <concurrentBuild>false</concurrentBuild>
      <builders>
        <com.g2one.hudson.grails.GrailsBuilder>
          <targets>test-app unit:</targets>
          <name>(Default)</name>
          <grailsWorkDir/>
          <projectWorkDir/>
          <projectBaseDir/>
          <serverPort/>
          <properties/>
          <forceUpgrade>false</forceUpgrade>
          <nonInteractive>true</nonInteractive>
        </com.g2one.hudson.grails.GrailsBuilder>
      </builders>
      <publishers>
        <hudson.tasks.ArtifactArchiver>
          <artifacts>/target/**</artifacts>
          <latestOnly>false</latestOnly>
        </hudson.tasks.ArtifactArchiver>
      </publishers>
      <buildWrappers>
        <org.jvnet.hudson.plugins.port__allocator.PortAllocator>
          <ports>
            <org.jvnet.hudson.plugins.port__allocator.DefaultPortType>
              <name>GRAILS_HTTP_PORT</name>
            </org.jvnet.hudson.plugins.port__allocator.DefaultPortType>
          </ports>
        </org.jvnet.hudson.plugins.port__allocator.PortAllocator>
      </buildWrappers>
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
            //jobs = ['OneA': new ByteArrayInputStream(configXml.getBytes()), 'OneB': new ByteArrayInputStream(configXml.getBytes()), 'OneC': new ByteArrayInputStream(configXml.getBytes())]
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
        
        jonName = 'TestJob-' + new Date().time
        jobs.put(jobName, new ByteArrayInputStream(configXml.getBytes()))
    }
    
    jobs.each { name, config ->
        jenkinsArgs = ['create-job', name]
        jenkinsInputStream = config

        executeJenkinsCommand()
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