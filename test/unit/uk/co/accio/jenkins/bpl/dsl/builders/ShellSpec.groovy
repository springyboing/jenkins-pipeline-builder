package uk.co.accio.jenkins.bpl.dsl.builders

import groovy.xml.XmlUtil
import org.custommonkey.xmlunit.Diff
import uk.co.accio.jenkins.bpl.dsl.AbstractDslTester
import uk.co.accio.jenkins.dsl.builders.ShellDelegate

class ShellSpec extends AbstractDslTester {

    Class delegateClass = ShellDelegate
    String rootName = ShellDelegate.name

    def 'Shell Builder XML'() {

        given:
            def shellXml = '''\
                    <?xml version="1.0" encoding="UTF-8"?>
                    '''.stripIndent()
            def delegate = new ShellDelegate()
            delegate.command = 'ls -lash'

        when:
            def theXml = toXml(delegate)
            def xmlDiff = new Diff(theXml, XmlUtil.serialize(shellXml))

        then:
            xmlDiff.identical()
    }
}