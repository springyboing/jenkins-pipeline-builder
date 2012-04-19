package uk.co.accio.jenkins.bpl.dsl.builders

import groovy.xml.StreamingMarkupBuilder
import groovy.xml.XmlUtil
import org.custommonkey.xmlunit.Diff
import uk.co.accio.jenkins.dsl.builders.GrailsDelegate
import uk.co.accio.jenkins.bpl.dsl.AbstractDslTester

class GrailsSpec extends AbstractDslTester {

    def setupSpec() {
        delegateClass = GrailsDelegate
        rootName = GrailsDelegate.name
    }

    def grailsBuilderXml = '''\
<?xml version="1.0" encoding="UTF-8"?>
<com.g2one.hudson.grails.GrailsBuilder>
<targets>test-app unit:</targets>
<name>(Default)</name>
<grailsWorkDir>./grailsWorkDir</grailsWorkDir>
<projectWorkDir>./projectWorkDir</projectWorkDir>
<projectBaseDir>./projectBaseDir</projectBaseDir>
<serverPort>GRAILS_HTTP_PORT</serverPort>
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
            delegate._serverPort = 'GRAILS_HTTP_PORT'
            delegate._properties = "propsA,propsB"
            delegate._forceUpgrade = true
            delegate._nonInteractive = true

        when:
            def theXml = toXml(delegate)
            def xmlDiff = new Diff(theXml, XmlUtil.serialize(grailsBuilderXml))

        then:
            xmlDiff.similar()
    }

    def 'Full Grails Builder DSL'() {

        given:
            def theDSL = '''\
                grails {
                        name '(Default)'
                        targets 'test-app unit:'
                        grailsWorkDir "./grailsWorkDir"
                        projectWorkDir './projectWorkDir'
                        projectBaseDir './projectBaseDir'
                        serverPort 'GRAILS_HTTP_PORT'
                        properties 'propsA,propsB'
                        forceUpgrade true
                        nonInteractive true
                }
                '''

        when:
            def theXml = dslToXml(theDSL)
            def xmlDiff = new Diff(theXml, XmlUtil.serialize(grailsBuilderXml))

        then:
            xmlDiff.similar()
    }
}