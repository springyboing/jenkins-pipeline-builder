package uk.co.accio.jenkins.bpl.dsl.other

import grails.plugin.spock.UnitSpec

import org.custommonkey.xmlunit.Diff
import org.custommonkey.xmlunit.XMLUnit

import groovy.xml.XmlUtil
import uk.co.accio.jenkins.bpl.dsl.AbstractDslTester
import uk.co.accio.jenkins.dsl.scms.svn.Location
import uk.co.accio.jenkins.dsl.scms.svn.LocationDelegate
import uk.co.accio.jenkins.dsl.other.Jdk
import uk.co.accio.jenkins.dsl.other.JdkDelegate

class JdkSpec extends AbstractDslTester {

    Class delegateClass = JdkDelegate
    String rootName = JdkDelegate.name

    def jdkXml = '''\
        <?xml version="1.0" encoding="UTF-8"?>
        <jdk>Java7</jdk>'''.stripIndent()

    def 'JDK to XML'() {

        given:
            def delegate = new JdkDelegate()
            delegate._value = 'Java7'

        when:
            def theXml = toXml(delegate)
            def xmlDiff = new Diff(theXml, XmlUtil.serialize(jdkXml))
            
        then:
            xmlDiff.identical()
    }

    def 'JDK DSL to bean'() {

        given:
            def theDSL = '''\
            jdk {
                jdk 'Java7'
            }'''.stripIndent()

        when:
            Jdk jdk = dslToObject(theDSL)

        then:
            jdk._value == 'Java7'
    }
    
}
