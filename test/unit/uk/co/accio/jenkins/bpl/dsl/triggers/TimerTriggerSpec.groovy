package uk.co.accio.jenkins.bpl.dsl.triggers

import grails.plugin.spock.UnitSpec
import groovy.xml.StreamingMarkupBuilder
import groovy.xml.XmlUtil
import org.custommonkey.xmlunit.Diff
import org.custommonkey.xmlunit.XMLUnit
import uk.co.accio.jenkins.dsl.triggers.ScmDelegate
import uk.co.accio.jenkins.dsl.triggers.TimerDelegate

class TimerTriggerSpec extends UnitSpec {

    def setupSpec() {
        XMLUnit.setIgnoreWhitespace(true)
        XMLUnit.setNormalizeWhitespace(true)
        XMLUnit.setNormalize(true)
    }

    def scmTriggerXml = '''\
<?xml version="1.0" encoding="UTF-8"?>
<hudson.triggers.TimerTrigger>
  <spec>5 * * * *</spec>
</hudson.triggers.TimerTrigger>
'''

    def 'Timer Trigger XML'() {

        given:
            def delegate = new TimerDelegate()
            delegate.spec = '5 * * * *'

        when:
            def theXml = toXml(delegate)
            def xmlDiff = new Diff(theXml, XmlUtil.serialize(scmTriggerXml))

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