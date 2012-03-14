package uk.co.accio.jenkins.bpl.dsl

import grails.plugin.spock.UnitSpec
import groovy.xml.StreamingMarkupBuilder
import groovy.xml.XmlUtil
import org.custommonkey.xmlunit.DetailedDiff
import org.custommonkey.xmlunit.Diff
import org.custommonkey.xmlunit.Difference
import org.custommonkey.xmlunit.XMLUnit
import uk.co.accio.jenkins.dsl.builders.CopyArtifactDelegate
import uk.co.accio.jenkins.dsl.BuildJobDelegate
import spock.lang.Ignore

class BuildJobSpec extends UnitSpec {

    def setupSpec() {
        XMLUnit.setIgnoreWhitespace(true)
        XMLUnit.setNormalizeWhitespace(true)
        XMLUnit.setNormalize(true)
    }

    def 'Simple BuildJob DSL'() {

        given:
            def desc = 'My First Build'
            def theDSL = """\
                buildJob() {
                }
                """

        when:
            def buildJob = dslToBuildJob(theDSL)

        then:
            buildJob
    }

    def 'Simple BuildJob DSL with only description'() {

        given:
            def desc = 'My First Build'
            def theDSL = """\
                buildJob() {
                    description '${desc}'
                }
                """

        when:
            def buildJob = dslToBuildJob(theDSL)

        then:
            buildJob.description == desc
    }

    def 'Simplest BuildJob DSL possible'() {

        setup:
            String theDsl = '''buildJob { }'''
            String configAsXml = '''\
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
                </project>'''.stripIndent()

        when:
            def buildJob = dslToBuildJob(theDsl)
            def buildXml = toBuildConfig(buildJob)
            println buildXml
            def xmlDiff = new Diff(configAsXml, buildXml)

        then:
            xmlDiff.similar()
    }

    def 'Simplest BuildJob DSL with trigger'() {

        setup:
            String theDsl = """\
                buildJob {
                    triggers {
                        timer {
                            spec '5 * * * *'
                        }
                    }
                }"""
            String configAsXml = '''\
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
                  <triggers class="vector">
                    <hudson.triggers.TimerTrigger>
                        <spec>5 * * * *</spec>
                    </hudson.triggers.TimerTrigger>
                  </triggers>
                  <concurrentBuild>false</concurrentBuild>
                  <builders/>
                  <publishers/>
                  <buildWrappers/>
                </project>'''.stripIndent()

        when:
            def buildJob = dslToBuildJob(theDsl)
            def buildXml = toBuildConfig(buildJob)
            def xmlDiff = new Diff(configAsXml, buildXml)

        then:
            xmlDiff.identical()
    }

    def 'Simplest BuildJob DSL with scm'() {

        setup:
            String theDsl = """\
                buildJob {
                    scms {
                        git {
                            userRemoteConfig {
                                name ''
                                refspec ''
                                url 'ssh://qwerty.com'
                            }
                        }
                    }
                }"""
            String configAsXml = '''\
                <?xml version='1.0' encoding='UTF-8'?>
                <project>
                  <actions/>
                  <keepDependencies>false</keepDependencies>
                  <properties/>
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
                  </scm>
                  <canRoam>false</canRoam>
                  <disabled>false</disabled>
                  <blockBuildWhenDownstreamBuilding>false</blockBuildWhenDownstreamBuilding>
                  <blockBuildWhenUpstreamBuilding>false</blockBuildWhenUpstreamBuilding>
                  <triggers class="vector"/>
                  <concurrentBuild>false</concurrentBuild>
                  <builders/>
                  <publishers/>
                  <buildWrappers/>
                </project>'''.stripIndent()

        when:
            def buildJob = dslToBuildJob(theDsl)
            def buildXml = toBuildConfig(buildJob)
            def xmlDiff = new Diff(configAsXml, buildXml)

        then:
//            buildXml == configAsXml
            xmlDiff.identical()
    }

    def 'Simplest BuildJob DSL with builder'() {

        setup:
            String theDsl = """\
                buildJob {
                    builders {
                        shell {
                            command 'ls -lash'
                        }
                    }
                }"""
            String configAsXml = '''\
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
                  <builders>
                    <hudson.tasks.Shell>
                      <command>ls -lash</command>
                    </hudson.tasks.Shell>
                  </builders>
                  <publishers/>
                  <buildWrappers/>
                </project>'''.stripIndent()

        when:
            def buildJob = dslToBuildJob(theDsl)
            def buildXml = toBuildConfig(buildJob)
            def xmlDiff = new Diff(configAsXml, buildXml)

        then:
            xmlDiff.identical()
    }

    def 'Simplest BuildJob DSL with publisher'() {

        setup:
            String theDsl = """\
                buildJob {
                    publishers {
                        artifactArchiver {
                            artifacts '/target/**'
                        }
                    }
                }"""
            String configAsXml = '''\
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
                  <publishers>
                    <hudson.tasks.ArtifactArchiver>
                      <artifacts>/target/**</artifacts>
                      <latestOnly>false</latestOnly>
                    </hudson.tasks.ArtifactArchiver>
                  </publishers>
                  <buildWrappers/>
                </project>'''.stripIndent()

        when:
            def buildJob = dslToBuildJob(theDsl)
            def buildXml = toBuildConfig(buildJob)
            def xmlDiff = new Diff(configAsXml, buildXml)

        then:
            xmlDiff.identical()
    }

    def 'Simplest BuildJob DSL with buildWrapper'() {

        setup:
            String theDsl = """\
                buildJob {
                    buildWrappers {
                        portAllocator {
                            ports {
                                name 'GRAILS_HTTP_PORT'
                            }
                        }
                    }
                }"""
            String configAsXml = '''\
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
                  <buildWrappers>
                    <org.jvnet.hudson.plugins.port__allocator.PortAllocator>
                      <ports>
                        <org.jvnet.hudson.plugins.port__allocator.DefaultPortType>
                          <name>GRAILS_HTTP_PORT</name>
                        </org.jvnet.hudson.plugins.port__allocator.DefaultPortType>
                      </ports>
                    </org.jvnet.hudson.plugins.port__allocator.PortAllocator>
                  </buildWrappers>
                </project>'''.stripIndent()

        when:
            def buildJob = dslToBuildJob(theDsl)
            def buildXml = toBuildConfig(buildJob)
            def xmlDiff = new Diff(configAsXml, buildXml)

        then:
            xmlDiff.identical()
    }

    def dslToBuildJob(String dslText) {
        def delegate
        Script dslScript = new GroovyShell().parse(dslText)
        dslScript.metaClass = createEMC(dslScript.class, {
            ExpandoMetaClass emc ->
                emc.buildJob = { Closure cl ->
                    cl.delegate = new BuildJobDelegate()
                    cl.resolveStrategy = Closure.DELEGATE_FIRST
                    cl()
                    delegate = cl.delegate
                }
        })
        dslScript.run()

        return delegate
    }

    static ExpandoMetaClass createEMC(Class clazz, Closure cl) {
        ExpandoMetaClass emc = new ExpandoMetaClass(clazz, false)
        cl(emc)
        emc.initialize()
        return emc
    }

    String toBuildConfig(buildJob) {
        def writer = new StringWriter()
        def builder = new StreamingMarkupBuilder().bind {
            mkp.xmlDeclaration()
            out << buildJob
        }
        writer << builder
        return XmlUtil.serialize(writer.toString())
    }
}
