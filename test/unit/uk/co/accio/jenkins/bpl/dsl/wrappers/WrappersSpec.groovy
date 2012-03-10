package uk.co.accio.jenkins.bpl.dsl.wrappers

import uk.co.accio.jenkins.dsl.wrappers.PortAllocatorDelegate.DefaultPortAllocatorDelegate

import grails.plugin.spock.UnitSpec
import groovy.xml.StreamingMarkupBuilder
import groovy.xml.XmlUtil
import org.custommonkey.xmlunit.Diff
import org.custommonkey.xmlunit.XMLUnit
import uk.co.accio.jenkins.dsl.wrappers.DefaultPortAllocatorDelegate
import uk.co.accio.jenkins.dsl.wrappers.PortAllocatorDelegate

class WrappersSpec extends UnitSpec {

    def setupSpec() {
        XMLUnit.setIgnoreWhitespace(true)
        XMLUnit.setNormalizeWhitespace(true)
        XMLUnit.setNormalize(true)
    }

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
</org.jvnet.hudson.plugins.port__allocator.PortAllocator>
'''

    def 'Port Allocator XML'() {

        given:
            def delegate = new PortAllocatorDelegate()
            def defaultPort = new DefaultPortAllocatorDelegate()
            defaultPort.name = 'GRAILS_HTTP_PORT'
            def defaultPort2 = new DefaultPortAllocatorDelegate()
            defaultPort2.name = 'HTTP_PORT'
            delegate.ports << defaultPort
            delegate.ports << defaultPort2

        when:
            def theXml = toXml(delegate)
            def xmlDiff = new Diff(theXml, XmlUtil.serialize(portWrapperXml))

        then:
            xmlDiff.identical()
    }

    String toXml(object) {
        def writer = new StringWriter()
        def builder = new StreamingMarkupBuilder().bind {
            out << object
        }
        writer << builder
        return XmlUtil.serialize(writer.toString())
    }
}