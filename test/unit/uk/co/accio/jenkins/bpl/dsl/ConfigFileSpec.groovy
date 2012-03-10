package uk.co.accio.jenkins.bpl.dsl

import grails.plugin.spock.UnitSpec
import org.custommonkey.xmlunit.Diff
import org.custommonkey.xmlunit.XMLUnit

class ConfigFileSpec extends UnitSpec {

    def setupSpec() {
        XMLUnit.setIgnoreWhitespace(true)
        XMLUnit.setNormalizeWhitespace(true)
        XMLUnit.setNormalize(true)
    }

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
