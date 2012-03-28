package uk.co.accio.jenkins.bpl.dsl

import grails.plugin.spock.UnitSpec
import org.custommonkey.xmlunit.Diff
import org.custommonkey.xmlunit.XMLUnit
import uk.co.accio.jenkins.dsl.JenkinsBuildsDelegate
import spock.lang.Ignore

class ConfigFileSpec extends UnitSpec {

    def setupSpec() {
        XMLUnit.setIgnoreWhitespace(true)
        XMLUnit.setNormalizeWhitespace(true)
        XMLUnit.setNormalize(true)
    }

    def "compair simplest config file possible"() {
        setup:
            String expectedXml = loadFile('config-minimal.xml')
            String personAsXml = """\
                <?xml version='1.0' encoding='UTF-8'?>
                <project>
                  <actions/>
                  <description/>
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

        when:
            def xmlDiff = new Diff(personAsXml, expectedXml)

        then:
            xmlDiff.identical()
    }

    @Ignore
    def 'Build defaults'() {

        setup:
            String theDsl = '''\
                builds {
                    defaults {

                    }
                }'''.stripIndent()
        
            String configAsXml = '''\
                <?xml version='1.0' encoding='UTF-8'?>
                <project>
                  <actions/>
                  <description/>
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
            def expectedXml = dslToXml(theDsl)
            def xmlDiff = new Diff(configAsXml, expectedXml)

        then:
            xmlDiff.identical()
    }

    def 'Simplest DSL possible'() {

        setup:
            String theDsl = '''\
                builds {
                    buildJob("${appName}-Checkout") {
                    }
                }'''.stripIndent()

            String configAsXml = '''\
                <?xml version='1.0' encoding='UTF-8'?>
                <project>
                  <actions/>
                  <description/>
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
            def jenkinsBuildConfigs = dslToJenkinsBuildConfigs(theDsl)
            def expectedXml = uk.co.accio.jenkins.dsl.JenkinsBuildsDelegate.toBuildConfig(jenkinsBuildConfigs.buildJobs[0])
            def xmlDiff = new Diff(configAsXml, expectedXml)

        then:
            xmlDiff.identical()
            jenkinsBuildConfigs.buildJobs[0].name == 'BottomBurp-Checkout'
    }


    String loadFile(name) {
        def value
        def inputStream
        try {
            inputStream = getClass().getClassLoader().getResourceAsStream(name)
            value = inputStream.text
        } finally {
            org.apache.commons.io.IOUtils.closeQuietly(inputStream)
        }
        return value
    }
    
    def dslToJenkinsBuildConfigs(String dslText) {
        def delegate
        Binding binding = new Binding()
        binding.setVariable("appName", 'BottomBurp')
        Script dslScript = new GroovyShell(binding).parse(dslText)
        dslScript.metaClass = createEMC(dslScript.class, {
            ExpandoMetaClass emc ->
                emc.builds = { Closure cl ->
                    cl.delegate = new JenkinsBuildsDelegate()
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
}
