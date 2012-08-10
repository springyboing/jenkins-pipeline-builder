package uk.co.accio.jenkins.bpl.dsl.scms

import grails.plugin.spock.UnitSpec

import org.custommonkey.xmlunit.Diff
import org.custommonkey.xmlunit.XMLUnit

import groovy.xml.XmlUtil
import uk.co.accio.jenkins.bpl.dsl.AbstractDslTester
import uk.co.accio.jenkins.dsl.scms.svn.SvnDelegate
import uk.co.accio.jenkins.dsl.scms.svn.LocationDelegate
import uk.co.accio.jenkins.dsl.scms.svn.Location

class SvnLocationSpec extends AbstractDslTester {

    Class delegateClass = LocationDelegate
    String rootName = LocationDelegate.name

    def svnLocationXml = '''\
        <?xml version="1.0" encoding="UTF-8"?>
        <hudson.scm.SubversionSCM_-ModuleLocation>
            <remote>http://example.com</remote>
            <local>.</local>
        </hudson.scm.SubversionSCM_-ModuleLocation>'''.stripIndent()

    def 'Svn Location to XML'() {

        given:
            def delegate = new Location()
            delegate._remote = 'http://example.com'
            delegate._local = '.'

        when:
            def theXml = toXml(delegate)
            def xmlDiff = new Diff(theXml, XmlUtil.serialize(svnLocationXml))
            
        then:
            xmlDiff.identical()
    }

    def 'Svn Location DSL to bean'() {

        given:
            def theDSL = '''\
                location {
                    remote 'http://example.com'
                    local '.'
                }'''.stripIndent()

        when:
            Location location = dslToObject(theDSL)

        then:
            location._remote == 'http://example.com'
            location._local == '.'
    }
    
}
