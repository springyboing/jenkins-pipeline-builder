package uk.co.accio.jenkins.bpl.dsl.publishers

import grails.plugin.spock.UnitSpec
import groovy.xml.StreamingMarkupBuilder
import groovy.xml.XmlUtil
import org.custommonkey.xmlunit.Diff
import org.custommonkey.xmlunit.XMLUnit
import uk.co.accio.jenkins.dsl.publishers.BuildTriggerDelegate
import uk.co.accio.jenkins.dsl.publishers.JUnitResultArchiverDelegate

class JUnitResultArchiverSpec extends UnitSpec {

    def setupSpec() {
        XMLUnit.setIgnoreWhitespace(true)
        XMLUnit.setNormalizeWhitespace(true)
        XMLUnit.setNormalize(true)
    }

    def jUnitResultArchiverXml = '''\
<?xml version="1.0" encoding="UTF-8"?>
<hudson.tasks.junit.JUnitResultArchiver>
      <testResults>target/**</testResults>
      <keepLongStdio>true</keepLongStdio>
      <testDataPublishers/>
</hudson.tasks.junit.JUnitResultArchiver>
'''

    def 'JUnit Result Archiver XML'() {

        given:
            def delegate = new JUnitResultArchiverDelegate()
            delegate.testResults = 'target/**'
            delegate.keepLongStdio = true

        when:
            def theXml = toXml(delegate)
            def xmlDiff = new Diff(theXml, XmlUtil.serialize(jUnitResultArchiverXml))

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