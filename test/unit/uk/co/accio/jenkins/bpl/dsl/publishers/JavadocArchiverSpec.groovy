package uk.co.accio.jenkins.bpl.dsl.publishers

import grails.plugin.spock.UnitSpec
import groovy.xml.StreamingMarkupBuilder
import groovy.xml.XmlUtil
import org.custommonkey.xmlunit.Diff
import org.custommonkey.xmlunit.XMLUnit
import uk.co.accio.jenkins.dsl.publishers.BuildTriggerDelegate
import uk.co.accio.jenkins.dsl.publishers.JavadocArchiverDelegate

class JavadocArchiverSpec extends UnitSpec {

    def setupSpec() {
        XMLUnit.setIgnoreWhitespace(true)
        XMLUnit.setNormalizeWhitespace(true)
        XMLUnit.setNormalize(true)
    }

    def javaDocXml = '''\
<?xml version="1.0" encoding="UTF-8"?>
<hudson.tasks.JavadocArchiver>
      <javadocDir>target/**</javadocDir>
      <keepAll>true</keepAll>
</hudson.tasks.JavadocArchiver>
'''

    def 'JavaDoc XML'() {

        given:
            def delegate = new JavadocArchiverDelegate()
            delegate.javadocDir = 'target/**'
            delegate.keepAll = true

        when:
            def theXml = toXml(delegate)
            def xmlDiff = new Diff(theXml, XmlUtil.serialize(javaDocXml))

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