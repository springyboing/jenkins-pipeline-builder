package uk.co.accio.jenkins.bpl.dsl.other

import grails.plugin.spock.UnitSpec

import org.custommonkey.xmlunit.Diff
import org.custommonkey.xmlunit.XMLUnit

import groovy.xml.XmlUtil
import uk.co.accio.jenkins.bpl.dsl.AbstractDslTester
import uk.co.accio.jenkins.dsl.other.Jdk
import uk.co.accio.jenkins.dsl.other.JdkDelegate
import uk.co.accio.jenkins.dsl.other.parameterize.StringParamDelegate
import uk.co.accio.jenkins.dsl.other.parameterize.StringParam

class StringParamSpec extends AbstractDslTester {

    Class delegateClass = StringParamDelegate
    String rootName = StringParamDelegate.name

    def 'String Param to XML'() {

        given:
            def theExpectedXml = '''\
                    <?xml version="1.0" encoding="UTF-8"?>
                    <hudson.model.StringParameterDefinition>
                      <name>SCM_REVISION</name>
                      <description>My Default</description>
                      <defaultValue>HEAD</defaultValue>
                    </hudson.model.StringParameterDefinition>
                    '''.stripIndent()
            def delegate = new StringParam()
            delegate._name = 'SCM_REVISION'
            delegate._description = 'My Default'
            delegate._defaultValue = 'HEAD'

        when:
            def theXml = toXml(delegate)
            def xmlDiff = new Diff(theXml, XmlUtil.serialize(theExpectedXml))
            
        then:
            xmlDiff.identical()
    }

    def 'JDK DSL to bean'() {

        given:
            def theDSL = '''\
            string {
                name 'SCM_REVISION'
                description 'My Default'
                defaultValue 'HEAD'
            }'''.stripIndent()

        when:
            StringParam param = dslToObject(theDSL)

        then:
            param._name == 'SCM_REVISION'
            param._description == 'My Default'
            param._defaultValue ==  'HEAD'
    }
}