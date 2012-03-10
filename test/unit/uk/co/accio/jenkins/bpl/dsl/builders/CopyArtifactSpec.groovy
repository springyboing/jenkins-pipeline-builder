package uk.co.accio.jenkins.bpl.dsl.builders

import grails.plugin.spock.UnitSpec
import groovy.xml.StreamingMarkupBuilder
import groovy.xml.XmlUtil
import org.custommonkey.xmlunit.DetailedDiff
import org.custommonkey.xmlunit.Diff
import org.custommonkey.xmlunit.Difference
import org.custommonkey.xmlunit.XMLUnit
import uk.co.accio.jenkins.dsl.builders.CopyArtifactDelegate

class CopyArtifactSpec extends UnitSpec {

    def setupSpec() {
        XMLUnit.setIgnoreWhitespace(true)
        XMLUnit.setNormalizeWhitespace(true)
        XMLUnit.setNormalize(true)
    }

    def copyArtifactXml = '''\
<?xml version="1.0" encoding="UTF-8"?>
<hudson.plugins.copyartifact.CopyArtifact>
  <projectName>BobJob-1330875179638</projectName>
  <filter>/**/*</filter>
  <target>blur</target>
  <selector class="hudson.plugins.copyartifact.TriggeredBuildSelector"/>
</hudson.plugins.copyartifact.CopyArtifact>
'''

    def 'Copy Artifact Builder XML'() {

        given:
            def delegate = new CopyArtifactDelegate()
            delegate.projectName = 'BobJob-1330875179638'
            delegate.filter = "/**/*"
            delegate.target = 'blur'

        when:
            def theXml = toXml(delegate)
            def xmlDiff = new Diff(theXml, XmlUtil.serialize(copyArtifactXml))
            DetailedDiff dd = new DetailedDiff(xmlDiff)
            dd.getAllDifferences().each {
                Difference d = (Difference) it
                println('---------------------------------------------')
                println(d)
            }

        then:
            xmlDiff.similar()
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
