package uk.co.accio.jenkins.bpl.dsl.builders

import grails.plugin.spock.UnitSpec
import groovy.xml.StreamingMarkupBuilder
import groovy.xml.XmlUtil
import org.custommonkey.xmlunit.DetailedDiff
import org.custommonkey.xmlunit.Diff
import org.custommonkey.xmlunit.Difference
import org.custommonkey.xmlunit.XMLUnit
import uk.co.accio.jenkins.dsl.builders.GrailsDelegate

class GrailsSpec extends UnitSpec {

    def setupSpec() {
        XMLUnit.setIgnoreWhitespace(true)
        XMLUnit.setNormalizeWhitespace(true)
        XMLUnit.setNormalize(true)
    }

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
            def delegate = new GrailsDelegate()
            delegate._targets = 'test-app unit:'
            delegate._name = "(Default)"
            delegate._grailsWorkDir = './grailsWorkDir'
            delegate._projectWorkDir = './projectWorkDir'
            delegate._projectBaseDir = './projectBaseDir'
            delegate._serverPort = '$GRAILS_HTTP_PORT'
            delegate._properties = "propsA,propsB"
            delegate._forceUpgrade = true
            delegate._nonInteractive = true

        when:
            def theXml = toXml(delegate)
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
