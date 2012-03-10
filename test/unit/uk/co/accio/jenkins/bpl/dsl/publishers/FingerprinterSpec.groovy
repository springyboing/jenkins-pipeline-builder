package uk.co.accio.jenkins.bpl.dsl.publishers

import grails.plugin.spock.UnitSpec
import groovy.xml.StreamingMarkupBuilder
import groovy.xml.XmlUtil
import org.custommonkey.xmlunit.Diff
import org.custommonkey.xmlunit.XMLUnit
import uk.co.accio.jenkins.dsl.publishers.BuildTriggerDelegate
import uk.co.accio.jenkins.dsl.publishers.FingerprinterDelegate

class FingerprinterSpec extends UnitSpec {

    def setupSpec() {
        XMLUnit.setIgnoreWhitespace(true)
        XMLUnit.setNormalizeWhitespace(true)
        XMLUnit.setNormalize(true)
    }

    def fingerprinterXml = '''\
<?xml version="1.0" encoding="UTF-8"?>
<hudson.tasks.Fingerprinter>
      <targets>target/**</targets>
      <recordBuildArtifacts>true</recordBuildArtifacts>
</hudson.tasks.Fingerprinter>
'''

    def 'Fingerprinter XML'() {

        given:
            def delegate = new FingerprinterDelegate()
            delegate.targets = 'target/**'
            delegate.recordBuildArtifacts = true

        when:
            def theXml = toXml(delegate)
            def xmlDiff = new Diff(theXml, XmlUtil.serialize(fingerprinterXml))

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