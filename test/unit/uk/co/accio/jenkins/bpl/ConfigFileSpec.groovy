package uk.co.accio.jenkins.bpl

import grails.plugin.spock.*
import org.custommonkey.xmlunit.*

class ConfigFileSpec extends UnitSpec {

    def "compair simplest config file possible"() {
        setup:
            String expectedXml = loadFile('config-minimal.xml')
            String personAsXml = """<?xml version='1.0' encoding='UTF-8'?>
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
</project>"""

        when:
            def xmlDiff = new Diff(personAsXml, expectedXml)

        then:
            xmlDiff.identical()
            //xmlDiff.similar()
    }

    def "create simplest config file possible"() {
        setup:
            String expectedXml = loadFile('config-minimal.xml')
            String dsl = """build {
                                name "Billy"
                                desc "My First Desc"
                            }
                         """
            String generatedXml = ''//uk.co.accio.jenkins.dsl.BuildConfiguration.runJenkinsBuilder(dsl)

        when:
            def xmlDiff = new Diff(generatedXml, expectedXml)

        then:
            xmlDiff.identical()
            //xmlDiff.similar()
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
}
