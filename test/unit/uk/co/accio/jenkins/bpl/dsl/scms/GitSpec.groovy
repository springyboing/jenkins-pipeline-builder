package uk.co.accio.jenkins.bpl.dsl.scms

import groovy.xml.XmlUtil
import org.custommonkey.xmlunit.DetailedDiff
import org.custommonkey.xmlunit.Diff
import org.custommonkey.xmlunit.Difference
import uk.co.accio.jenkins.dsl.scms.git.GitBranchDelegate
import uk.co.accio.jenkins.dsl.scms.git.GitDelegate
import uk.co.accio.jenkins.dsl.scms.git.GitUserRemoteConfigDelegate
import uk.co.accio.jenkins.bpl.dsl.AbstractDslTester

class GitSpec extends AbstractDslTester {

    def setupSpec() {
        delegateClass = GitDelegate
        rootName = GitDelegate.name
    }

    def gitScmXml = '''\
<?xml version="1.0" encoding="UTF-8"?>
<scm class="hudson.plugins.git.GitSCM">
<configVersion>36</configVersion>
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
<scmName>HelloWorld</scmName>
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

        then:
            xmlDiff.identical()
    }

    def 'Full Git SCM DSL'() {

        given:
            def theDSL = '''\
                git {
                    userRemoteConfig {
                        name ''
                        refspec ''
                        url 'ssh://qwerty.com'
                    }
                }
                '''
            def fullGitScmXml = '''\
                <?xml version="1.0" encoding="UTF-8"?>
                <scm class="hudson.plugins.git.GitSCM">
                    <configVersion>2</configVersion>
                    <userRemoteConfigs>
                      <hudson.plugins.git.UserRemoteConfig>
                        <name></name>
                        <refspec></refspec>
                        <url>ssh://qwerty.com</url>
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
                    <relativeTargetDir></relativeTargetDir>
                    <reference></reference>
                    <excludedRegions></excludedRegions>
                    <excludedUsers></excludedUsers>
                    <gitConfigName></gitConfigName>
                    <gitConfigEmail></gitConfigEmail>
                    <skipTag>false</skipTag>
                    <includedRegions></includedRegions>
                    <scmName></scmName>
                </scm>'''.stripIndent()

        when:
            def theXml = dslToXml(theDSL)
            def xmlDiff = new Diff(theXml, XmlUtil.serialize(fullGitScmXml))

        then:
            xmlDiff.similar()
    }

    def 'Minimal Git SCM DSL'() {

        given:
            def theDSL = '''\
                git {
                    userRemoteConfig {
                        name ''
                        refspec ''
                        url 'ssh://qwerty.com'
                    }
                }
                '''
            def minimalGitScmXml = '''\
                <?xml version="1.0" encoding="UTF-8"?>
                <scm class="hudson.plugins.git.GitSCM">
                    <configVersion>2</configVersion>
                    <userRemoteConfigs>
                      <hudson.plugins.git.UserRemoteConfig>
                        <name></name>
                        <refspec></refspec>
                        <url>ssh://qwerty.com</url>
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
                    <relativeTargetDir></relativeTargetDir>
                    <reference></reference>
                    <excludedRegions></excludedRegions>
                    <excludedUsers></excludedUsers>
                    <gitConfigName></gitConfigName>
                    <gitConfigEmail></gitConfigEmail>
                    <skipTag>false</skipTag>
                    <includedRegions></includedRegions>
                    <scmName></scmName>
                </scm>'''.stripIndent()

        when:
            def theXml = dslToXml(theDSL)
            def xmlDiff = new Diff(theXml, XmlUtil.serialize(minimalGitScmXml))

        then:
            xmlDiff.similar()
    }
}