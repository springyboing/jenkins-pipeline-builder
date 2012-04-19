package uk.co.accio.jenkins.bpl.dsl.triggers

import grails.plugin.spock.UnitSpec
import groovy.xml.StreamingMarkupBuilder
import groovy.xml.XmlUtil
import org.custommonkey.xmlunit.Diff
import org.custommonkey.xmlunit.XMLUnit
import uk.co.accio.jenkins.dsl.triggers.ScmDelegate
import uk.co.accio.jenkins.bpl.dsl.AbstractDslTester

class ScmTriggerSpec extends AbstractDslTester {

    Class delegateClass = ScmDelegate
    String rootName = ScmDelegate.name

    def 'Scm Trigger XML'() {

        given:
            def delegate = new ScmDelegate()
            delegate.spec = '5 * * * *'
            def scmTriggerXml = '''\
                <?xml version="1.0" encoding="UTF-8"?>
                <hudson.triggers.SCMTrigger>
                  <spec>5 * * * *</spec>
                </hudson.triggers.SCMTrigger>'''.stripIndent()

        when:
            def theXml = toXml(delegate)
            def xmlDiff = new Diff(theXml, XmlUtil.serialize(scmTriggerXml))

        then:
            xmlDiff.identical()
    }

    def 'Minimal SCM Trigger DSL'() {

        given:
            def theDSL = '''\
                scm {
                    spec '5 * * * *'
                }'''.stripIndent()
            def scmTriggerXml = '''\
                <?xml version="1.0" encoding="UTF-8"?>
                <hudson.triggers.SCMTrigger>
                  <spec>5 * * * *</spec>
                </hudson.triggers.SCMTrigger>'''.stripIndent()
        when:
            def theXml = dslToXml(theDSL)
            def xmlDiff = new Diff(theXml, XmlUtil.serialize(scmTriggerXml))

        then:
            xmlDiff.identical()
    }
}