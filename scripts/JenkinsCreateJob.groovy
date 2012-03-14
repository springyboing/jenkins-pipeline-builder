import hudson.cli.CLI

grailsHome = ant.project.properties."environment.GRAILS_HOME"
includeTargets << new File ( "${grailsHome}/scripts/Init.groovy" )

target(main: "Installs all the required plugins on the Jenkins server") {
	depends(configureProxy, classpath)

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

    def host = '192.168.1.68'
    def port = '8080'
    def jenkinsUrl = "http://${host}:${port}"

    runCliCommand(jenkinsUrl, ['create-job', 'BobJob-' + new Date().time], new ByteArrayInputStream(configXml.getBytes()))
}

setDefaultTarget(main)

def runCliCommand(String rootUrl, List<String> args, InputStream input = System.in, OutputStream output = System.out, OutputStream err = System.err) {
    def CLI cli = new CLI(rootUrl.toURI().toURL())
    cli.execute(args, input, output, err)
    cli.close()
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