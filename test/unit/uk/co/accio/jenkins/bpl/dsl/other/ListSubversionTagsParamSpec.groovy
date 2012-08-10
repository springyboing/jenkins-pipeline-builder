package uk.co.accio.jenkins.bpl.dsl.other

import grails.plugin.spock.UnitSpec

import org.custommonkey.xmlunit.Diff
import org.custommonkey.xmlunit.XMLUnit

import groovy.xml.XmlUtil
import uk.co.accio.jenkins.bpl.dsl.AbstractDslTester
import uk.co.accio.jenkins.dsl.other.parameterize.StringParam
import uk.co.accio.jenkins.dsl.other.parameterize.StringParamDelegate
import uk.co.accio.jenkins.dsl.other.parameterize.ListSubversionTagsParamDelegate
import uk.co.accio.jenkins.dsl.other.parameterize.ListSubversionTagsParam

class ListSubversionTagsParamSpec extends AbstractDslTester {

    Class delegateClass = ListSubversionTagsParamDelegate
    String rootName = ListSubversionTagsParamDelegate.name

    def 'List Subversion Tag Param to XML'() {

        given:
            def theExpectedXml = '''\
                    <?xml version="1.0" encoding="UTF-8"?>
                    <hudson.scm.listtagsparameter.ListSubversionTagsParameterDefinition>
                      <name>SCM_REVISION</name>
                      <description>MyDescription</description>
                      <tagsDir>MyDir</tagsDir>
                      <tagsFilter>MyFilter</tagsFilter>
                      <reverseByDate>true</reverseByDate>
                      <reverseByName>true</reverseByName>
                      <defaultValue>HEAD</defaultValue>
                      <maxTags>10</maxTags>
                    </hudson.scm.listtagsparameter.ListSubversionTagsParameterDefinition>
                    '''.stripIndent()
            def delegate = new ListSubversionTagsParam()
            delegate._name = 'SCM_REVISION'
            delegate._description = 'MyDescription'
            delegate._tagsDir = 'MyDir'
            delegate._tagsFilter = 'MyFilter'
            delegate._reverseByDate = true
            delegate._reverseByName = true
            delegate._maxTags = '10'
            delegate._defaultValue = 'HEAD'

        when:
            def theXml = toXml(delegate)
            def xmlDiff = new Diff(theXml, XmlUtil.serialize(theExpectedXml))
            
        then:
            xmlDiff.identical()
    }

    def 'List Subversion Tags DSL to bean'() {

        given:
            def theDSL = '''\
            svnTags {
                name 'SCM_REVISION'
                description 'MyDescription'
                tagsDir 'MyDir'
                tagsFilter 'MyFilter'
                reverseByDate true
                reverseByName false
                maxTags 10
                defaultValue 'HEAD'
            }'''.stripIndent()

        when:
            ListSubversionTagsParam param = dslToObject(theDSL)

        then:
            param._name == 'SCM_REVISION'
            param._description == 'MyDescription'
            param._tagsDir == 'MyDir'
            param._tagsFilter == 'MyFilter'
            param._reverseByDate == true
            param._reverseByName == false
            param._maxTags == '10'
            param._defaultValue ==  'HEAD'
    }
    
}

/*

                    */