package uk.co.accio.jenkins.bpl.dsl.scms

import grails.plugin.spock.UnitSpec
import groovy.xml.StreamingMarkupBuilder
import groovy.xml.XmlUtil
import org.custommonkey.xmlunit.DetailedDiff
import org.custommonkey.xmlunit.Diff
import org.custommonkey.xmlunit.Difference
import org.custommonkey.xmlunit.XMLUnit
import uk.co.accio.jenkins.dsl.scms.git.GitBranchDelegate
import uk.co.accio.jenkins.dsl.scms.git.GitDelegate
import uk.co.accio.jenkins.dsl.scms.git.GitUserRemoteConfigDelegate

class GitSpec extends UnitSpec {

    def setupSpec() {
        XMLUnit.setIgnoreWhitespace(true)
        XMLUnit.setNormalizeWhitespace(true)
        XMLUnit.setNormalize(true)
    }

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
            def delegate = new GitDelegate()
            delegate._scmName = 'HelloWorld'
            delegate._configVersion = 36
            delegate._gitConfigName = 'Joe Blogs'
            delegate._gitConfigEmail = 'joe@blogs.com'
            delegate._relativeTargetDir = 'A'
            delegate._reference = 'B'
            delegate._excludedRegions = 'C'
            delegate._excludedUsers = 'D'
            delegate._includedRegions = 'E'
            delegate._branches << new GitBranchDelegate(_name: 'master')
            delegate._userRemoteConfigs << new GitUserRemoteConfigDelegate(_name: 'MyGitRemote', _refspec: '/ref/HEAD', _url: 'git://github.com/springyboing/jenkins-pipeline-builder.git')

        when:
            def theXml = toXml(delegate)
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
