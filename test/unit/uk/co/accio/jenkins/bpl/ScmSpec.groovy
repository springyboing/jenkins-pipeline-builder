package uk.co.accio.jenkins.bpl

import grails.plugin.spock.UnitSpec
import uk.co.accio.jenkins.GitDelegate
import groovy.xml.StreamingMarkupBuilder
import groovy.xml.XmlUtil
import org.custommonkey.xmlunit.Diff
import uk.co.accio.jenkins.GitBranchDelegate
import uk.co.accio.jenkins.GitUserRemoteConfigDelegate
import org.custommonkey.xmlunit.XMLUnit

class ScmSpec extends UnitSpec {

    def gitScmXml = '''<scm class="hudson.plugins.git.GitSCM">
    <configVersion>36</configVersion>
    <scmName>HelloWorld</scmName>
    <userRemoteConfigs>
      <hudson.plugins.git.UserRemoteConfig>
        <name>MyGitRemote</name>
        <refspec>/ref/HEAD</refspec>
        <url>git://github.com/springyboing/jenkins-pipeline-builder.git</url>
      </hudson.plugins.git.UserRemoteConfig>
    </userRemoteConfigs>
    <branches>
      <hudson.plugins.git.BranchSpec>
        <name>master</name>
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
    <relativeTargetDir></relativeTargetDir>
    <reference></reference>
    <excludedRegions></excludedRegions>
    <excludedUsers></excludedUsers>
    <gitConfigName>Joe Blogs</gitConfigName>
    <gitConfigEmail>joe@blogs.com</gitConfigEmail>
    <skipTag>false</skipTag>
    <includedRegions></includedRegions>
  </scm>
'''

    def 'Git XML'() {

        given:
            def gitD = new GitDelegate()
            gitD._scmName = 'MyTestName'
            gitD._configVersion = 36
            gitD._gitConfigName = 'Joe Blogs'
            gitD._gitConfigEmail = 'joe@blogs.com'
            gitD._branches << new GitBranchDelegate(_name: 'MyGitBranch')
            gitD._userRemoteConfigs << new GitUserRemoteConfigDelegate(_name: 'MyGithRemote', _refspec: '/ref/HEAD', _url: 'git://github.com/springyboing/jenkins-pipeline-builder.git')

        when:
            def theXml = toXml(gitD)
            def xmlDiff = new Diff(theXml, gitScmXml)

        then:
            gitScmXml == theXml
            xmlDiff.identical()
        
    }

    String toXml(object) {
        def writer = new StringWriter()
        def builder = new StreamingMarkupBuilder().bind {
            mkp.xmlDeclaration()
            out << object
        }
        writer << builder
        return XmlUtil.serialize(writer.toString())
    }

}
