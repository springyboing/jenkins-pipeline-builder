package uk.co.accio.jenkins.bpl.dsl.publishers

import grails.plugin.spock.UnitSpec
import groovy.xml.StreamingMarkupBuilder
import groovy.xml.XmlUtil
import org.custommonkey.xmlunit.Diff
import org.custommonkey.xmlunit.XMLUnit
import uk.co.accio.jenkins.dsl.publishers.ArtifactArchiverDelegate
import uk.co.accio.jenkins.dsl.publishers.BuildTriggerDelegate
import uk.co.accio.jenkins.dsl.publishers.BuildTriggerThresholdDelegate

class BuildTriggerSpec extends UnitSpec {

    def setupSpec() {
        XMLUnit.setIgnoreWhitespace(true)
        XMLUnit.setNormalizeWhitespace(true)
        XMLUnit.setNormalize(true)
    }

    def buildTriggerXml = '''\
<?xml version="1.0" encoding="UTF-8"?>
<hudson.tasks.BuildTrigger>
<childProjects>My Child Build</childProjects>
  <threshold>
    <name>SUCCESS</name>
    <ordinal>0</ordinal>
    <color>BLUE</color>
  </threshold>
</hudson.tasks.BuildTrigger>
'''

    def 'Child Build XML'() {

        given:
            def delegate = new BuildTriggerDelegate()
            delegate.childProjects = 'My Child Build'
            delegate.threshold = new BuildTriggerThresholdDelegate(name: 'SUCCESS', ordinal: '0', color: 'BLUE')

        when:
            def theXml = toXml(delegate)
            def xmlDiff = new Diff(theXml, XmlUtil.serialize(buildTriggerXml))

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