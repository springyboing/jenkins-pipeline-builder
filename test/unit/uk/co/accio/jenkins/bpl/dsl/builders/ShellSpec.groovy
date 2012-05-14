package uk.co.accio.jenkins.bpl.dsl.builders

import groovy.xml.XmlUtil
import org.custommonkey.xmlunit.Diff
import uk.co.accio.jenkins.bpl.dsl.AbstractDslTester
import uk.co.accio.jenkins.dsl.builders.ShellDelegate
import uk.co.accio.jenkins.dsl.builders.Shell

class ShellSpec extends AbstractDslTester {

    Class delegateClass = ShellDelegate
    String rootName = ShellDelegate.name

    def 'Shell Builder XML'() {

        given:
            def shellXml = '''\
                    <?xml version="1.0" encoding="UTF-8"?>
                    <hudson.tasks.Shell>
                        <command>ls -lash</command>
                    </hudson.tasks.Shell>
                    '''.stripIndent()
            def delegate = new ShellDelegate()
            delegate.command = 'ls -lash'

        when:
            def theXml = toXml(delegate)
            def xmlDiff = new Diff(theXml, XmlUtil.serialize(shellXml))

        then:
            xmlDiff.identical()
    }

    def 'Complete Shell DSL'() {

        given:
            def theDSL = '''\
                    shell {
                        command 'ls -lash'
                    }'''.stripIndent()

        when:
            Shell shell = dslToObject(theDSL)

        then:
            shell.command == 'ls -lash'
    }
}