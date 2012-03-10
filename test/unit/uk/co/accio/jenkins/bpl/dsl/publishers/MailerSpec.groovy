package uk.co.accio.jenkins.bpl.dsl.publishers

import grails.plugin.spock.UnitSpec
import groovy.xml.StreamingMarkupBuilder
import groovy.xml.XmlUtil
import org.custommonkey.xmlunit.Diff
import org.custommonkey.xmlunit.XMLUnit
import uk.co.accio.jenkins.dsl.publishers.BuildTriggerDelegate
import uk.co.accio.jenkins.dsl.publishers.MailerDelegate

class MailerSpec extends UnitSpec {

    def setupSpec() {
        XMLUnit.setIgnoreWhitespace(true)
        XMLUnit.setNormalizeWhitespace(true)
        XMLUnit.setNormalize(true)
    }


    def emailerXml = '''\
<?xml version="1.0" encoding="UTF-8"?>
<hudson.tasks.Mailer>
  <recipients>joe.bloggs@example.com</recipients>
  <dontNotifyEveryUnstableBuild>true</dontNotifyEveryUnstableBuild>
  <sendToIndividuals>true</sendToIndividuals>
</hudson.tasks.Mailer>
'''

    def 'Emailer XML'() {

        given:
            def delegate = new MailerDelegate()
            delegate.recipients = 'joe.bloggs@example.com'
            delegate.dontNotifyEveryUnstableBuild = true
            delegate.sendToIndividuals = true

        when:
            def theXml = toXml(delegate)
            def xmlDiff = new Diff(theXml, XmlUtil.serialize(emailerXml))

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