package uk.co.accio.jenkins.bpl.dsl.publishers

import grails.plugin.spock.UnitSpec
import groovy.xml.StreamingMarkupBuilder
import groovy.xml.XmlUtil
import org.custommonkey.xmlunit.Diff
import org.custommonkey.xmlunit.XMLUnit
import uk.co.accio.jenkins.dsl.publishers.MailerDelegate

class BatchTaskInvokerSpec extends UnitSpec {

    def 'Batch Task Invoker XML'() {

        given:
            def p = 'blur'

        when:
            def theXml = 'blur'


        then:
            false
    }
}