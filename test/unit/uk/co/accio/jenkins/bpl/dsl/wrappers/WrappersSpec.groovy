package uk.co.accio.jenkins.bpl.dsl.wrappers

import grails.plugin.spock.UnitSpec
import groovy.xml.StreamingMarkupBuilder
import groovy.xml.XmlUtil
import org.custommonkey.xmlunit.Diff
import org.custommonkey.xmlunit.XMLUnit
import uk.co.accio.jenkins.dsl.wrappers.DefaultPortAllocatorDelegate
import uk.co.accio.jenkins.dsl.wrappers.PortAllocatorDelegate
import uk.co.accio.jenkins.bpl.dsl.AbstractDslTester

class WrappersSpec extends AbstractDslTester {

    Class delegateClass = PortAllocatorDelegate
    String rootName = PortAllocatorDelegate.name

    def 'Port Allocator XML'() {

        given:
            def portWrapperXml = '''\
                    <?xml version="1.0" encoding="UTF-8"?>
                    <org.jvnet.hudson.plugins.port__allocator.PortAllocator>
                      <ports>
                        <org.jvnet.hudson.plugins.port__allocator.DefaultPortType>
                          <name>GRAILS_HTTP_PORT</name>
                        </org.jvnet.hudson.plugins.port__allocator.DefaultPortType>
                        <org.jvnet.hudson.plugins.port__allocator.DefaultPortType>
                          <name>HTTP_PORT</name>
                        </org.jvnet.hudson.plugins.port__allocator.DefaultPortType>
                      </ports>
                    </org.jvnet.hudson.plugins.port__allocator.PortAllocator>'''.stripIndent()

            def delegate = new PortAllocatorDelegate()
            delegate.ports << new DefaultPortAllocatorDelegate(name: 'GRAILS_HTTP_PORT')
            delegate.ports << new DefaultPortAllocatorDelegate(name: 'HTTP_PORT')

        when:
            def theXml = toXml(delegate)
            def xmlDiff = new Diff(theXml, XmlUtil.serialize(portWrapperXml))

        then:
            xmlDiff.identical()
    }

    def 'Port Allocator DSL'() {

        given:
            def theDSL = '''\
                portAllocator {
                    ports {
                        name 'GRAILS_HTTP_PORT'
                    }
                }'''.stripIndent()
            def theExpectedXml = '''\
                <?xml version="1.0" encoding="UTF-8"?>
                <org.jvnet.hudson.plugins.port__allocator.PortAllocator>
                    <ports>
                        <org.jvnet.hudson.plugins.port__allocator.DefaultPortType>
                            <name>GRAILS_HTTP_PORT</name>
                        </org.jvnet.hudson.plugins.port__allocator.DefaultPortType>
                    </ports>
                </org.jvnet.hudson.plugins.port__allocator.PortAllocator>'''.stripIndent()
        when:
            def theXml = dslToXml(theDSL)
            def xmlDiff = new Diff(theXml, XmlUtil.serialize(theExpectedXml))

        then:
            xmlDiff.identical()
    }
//
//    def dslToXml(String dslText) {
//        def delegate
//        Script dslScript = new GroovyShell().parse(dslText)
//        dslScript.metaClass = createEMC(dslScript.class, {
//            ExpandoMetaClass emc ->
//                emc.portAllocator = { Closure cl ->
//                    cl.delegate = new PortAllocatorDelegate()
//                    cl.resolveStrategy = Closure.DELEGATE_FIRST
//                    cl()
//                    delegate = cl.delegate
//                }
//        })
//        dslScript.run()
//
//        def writer = new StringWriter()
//        def builder = new StreamingMarkupBuilder().bind {
//            mkp.xmlDeclaration()
//            out << delegate
//        }
//        writer << builder
//        return XmlUtil.serialize(writer.toString())
//    }
}