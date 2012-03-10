package uk.co.accio.jenkins.bpl

import grails.plugin.spock.UnitSpec
import groovy.xml.StreamingMarkupBuilder
import groovy.xml.XmlUtil
import org.custommonkey.xmlunit.DetailedDiff
import org.custommonkey.xmlunit.Diff
import org.custommonkey.xmlunit.Difference
import org.custommonkey.xmlunit.XMLUnit

import uk.co.accio.jenkins.dsl.builders.GrailsDelegate

class GrailsSpec extends UnitSpec {

    def grailsBuilderXml = '''\
<?xml version="1.0" encoding="UTF-8"?>
<com.g2one.hudson.grails.GrailsBuilder>
<targets>test-app unit:</targets>
<name>(Default)</name>
<grailsWorkDir>./grailsWorkDir</grailsWorkDir>
<projectWorkDir>./projectWorkDir</projectWorkDir>
<projectBaseDir>./projectBaseDir</projectBaseDir>
<serverPort>$GRAILS_HTTP_PORT</serverPort>
<properties>propsA,propsB</properties>
<forceUpgrade>true</forceUpgrade>
<nonInteractive>true</nonInteractive>
</com.g2one.hudson.grails.GrailsBuilder>
'''

    def 'Grails Builder XML'() {

        given:
            XMLUnit.setIgnoreWhitespace(true)
            XMLUnit.setNormalizeWhitespace(true)
            XMLUnit.setNormalize(true)

            def grailsDelegate = new GrailsDelegate()
            grailsDelegate._targets = 'test-app unit:'
            grailsDelegate._name = "(Default)"
            grailsDelegate._grailsWorkDir = './grailsWorkDir'
            grailsDelegate._projectWorkDir = './projectWorkDir'
            grailsDelegate._projectBaseDir = './projectBaseDir'
            grailsDelegate._serverPort = '$GRAILS_HTTP_PORT'
            grailsDelegate._properties = "propsA,propsB"
            grailsDelegate._forceUpgrade = true
            grailsDelegate._nonInteractive = true

        when:
            def theXml = toXml(grailsDelegate)
            def xmlDiff = new Diff(theXml, XmlUtil.serialize(grailsBuilderXml))
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
