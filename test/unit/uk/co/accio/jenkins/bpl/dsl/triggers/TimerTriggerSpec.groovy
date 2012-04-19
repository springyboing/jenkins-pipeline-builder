package uk.co.accio.jenkins.bpl.dsl.triggers

import grails.plugin.spock.UnitSpec
import groovy.xml.StreamingMarkupBuilder
import groovy.xml.XmlUtil
import org.custommonkey.xmlunit.Diff
import org.custommonkey.xmlunit.XMLUnit
import uk.co.accio.jenkins.dsl.triggers.ScmDelegate
import uk.co.accio.jenkins.dsl.triggers.TimerDelegate
import uk.co.accio.jenkins.bpl.dsl.AbstractDslTester

class TimerTriggerSpec extends AbstractDslTester {

    Class delegateClass = TimerDelegate
    String rootName = TimerDelegate.name

    def 'Timer Trigger XML'() {

        given:
            def scmTriggerXml = '''\
                    <?xml version="1.0" encoding="UTF-8"?>
                    <hudson.triggers.TimerTrigger>
                      <spec>5 * * * *</spec>
                    </hudson.triggers.TimerTrigger>'''.stripIndent()
            def delegate = new TimerDelegate()
            delegate.spec = '5 * * * *'

        when:
            def theXml = toXml(delegate)
            def xmlDiff = new Diff(theXml, XmlUtil.serialize(scmTriggerXml))

        then:
            xmlDiff.identical()
    }

    def 'Timer Trigger DSL'() {

        given:
            def theDSL = '''\
                timer {
                    spec '5 * * * *'
                }'''.stripIndent()
            def timerTriggerXml = '''\
                <?xml version="1.0" encoding="UTF-8"?>
                <hudson.triggers.TimerTrigger>
                    <spec>5 * * * *</spec>
                </hudson.triggers.TimerTrigger>'''.stripIndent()
        when:
            def theXml = dslToXml(theDSL)
            def xmlDiff = new Diff(theXml, XmlUtil.serialize(timerTriggerXml))

        then:
            xmlDiff.identical()
    }
}