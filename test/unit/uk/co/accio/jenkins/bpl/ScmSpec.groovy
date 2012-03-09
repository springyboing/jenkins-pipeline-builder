package uk.co.accio.jenkins.bpl

import grails.plugin.spock.UnitSpec
import uk.co.accio.jenkins.GitDelegate
import groovy.xml.StreamingMarkupBuilder
import groovy.xml.XmlUtil
import org.custommonkey.xmlunit.Diff
import uk.co.accio.jenkins.GitBranchDelegate
import uk.co.accio.jenkins.GitUserRemoteConfigDelegate
import org.custommonkey.xmlunit.XMLUnit
import org.custommonkey.xmlunit.DetailedDiff
import org.custommonkey.xmlunit.Difference

class ScmSpec extends UnitSpec {

    def gitScmXml = '''\
<?xml version="1.0" encoding="UTF-8"?>
<scm class="hudson.plugins.git.GitSCM">
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
<relativeTargetDir>A</relativeTargetDir>
<reference>B</reference>
<excludedRegions>C</excludedRegions>
<excludedUsers>D</excludedUsers>
<gitConfigName>Joe Blogs</gitConfigName>
<gitConfigEmail>joe@blogs.com</gitConfigEmail>
<skipTag>false</skipTag>
<includedRegions>E</includedRegions>
</scm>
'''

    def 'Git XML'() {

        given:
            XMLUnit.setIgnoreWhitespace(true)
            XMLUnit.setNormalizeWhitespace(true)
            XMLUnit.setNormalize(true)

            def gitD = new GitDelegate()
            gitD._scmName = 'HelloWorld'
            gitD._configVersion = 36
            gitD._gitConfigName = 'Joe Blogs'
            gitD._gitConfigEmail = 'joe@blogs.com'
            gitD._relativeTargetDir = 'A'
            gitD._reference = 'B'
            gitD._excludedRegions = 'C'
            gitD._excludedUsers = 'D'
            gitD._includedRegions = 'E'
            gitD._branches << new GitBranchDelegate(_name: 'master')
            gitD._userRemoteConfigs << new GitUserRemoteConfigDelegate(_name: 'MyGitRemote', _refspec: '/ref/HEAD', _url: 'git://github.com/springyboing/jenkins-pipeline-builder.git')

        when:
            def theXml = toXml(gitD)
            def xmlDiff = new Diff(theXml, XmlUtil.serialize(gitScmXml))
            DetailedDiff dd = new DetailedDiff(xmlDiff)
            dd.getAllDifferences().each {
                Difference d = (Difference) it
                println('---------------------------------------------')
                println(d)
            }

        then:
            xmlDiff.identical()
    }

    String toXml(object) {
        def writer = new StringWriter()
        def builder = new StreamingMarkupBuilder().bind {
            out << object
        }
        writer << builder
        return XmlUtil.serialize(writer.toString())
    }

}
