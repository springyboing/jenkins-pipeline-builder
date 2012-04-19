package uk.co.accio.jenkins.bpl.dsl.scms

import grails.plugin.spock.UnitSpec
import groovy.xml.StreamingMarkupBuilder
import groovy.xml.XmlUtil
import org.custommonkey.xmlunit.Diff
import org.custommonkey.xmlunit.XMLUnit
import uk.co.accio.jenkins.dsl.scms.svn.SvnDelegate
import uk.co.accio.jenkins.bpl.dsl.AbstractDslTester

class SvnSpec extends AbstractDslTester {

    Class delegateClass = SvnDelegate
    String rootName = SvnDelegate.name

    def svnScmXml = '''\
<?xml version="1.0" encoding="UTF-8"?>
<scm class="hudson.scm.SubversionSCM">
    <locations/>
    <excludedRegions>/out</excludedRegions>
    <includedRegions>/src</includedRegions>
    <excludedUsers>Joe</excludedUsers>
    <excludedRevprop>Boo</excludedRevprop>
    <excludedCommitMessages>Blur</excludedCommitMessages>
    <workspaceUpdater class="hudson.scm.subversion.UpdateUpdater"/>
  </scm>
'''

    def 'Svn XML'() {

        given:
            def delegate = new SvnDelegate()
            delegate.excludedRegions = '/out'
            delegate.includedRegions = '/src'
            delegate.excludedUsers = 'Joe'
            delegate.excludedRevprop = 'Boo'
            delegate.excludedCommitMessages = 'Blur'
            
        when:
            def theXml = toXml(delegate)
            def xmlDiff = new Diff(theXml, XmlUtil.serialize(svnScmXml))
            
        then:
            xmlDiff.identical()
    }
}
