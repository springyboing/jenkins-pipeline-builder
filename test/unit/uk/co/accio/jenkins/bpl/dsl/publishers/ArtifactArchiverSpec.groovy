package uk.co.accio.jenkins.bpl.dsl.publishers

import grails.plugin.spock.UnitSpec
import groovy.xml.StreamingMarkupBuilder
import groovy.xml.XmlUtil
import org.custommonkey.xmlunit.Diff
import org.custommonkey.xmlunit.XMLUnit
import uk.co.accio.jenkins.dsl.publishers.ArtifactArchiverDelegate

class ArtifactArchiverSpec extends UnitSpec {

    def setupSpec() {
        XMLUnit.setIgnoreWhitespace(true)
        XMLUnit.setNormalizeWhitespace(true)
        XMLUnit.setNormalize(true)
    }

    def artifactXml = '''\
<?xml version="1.0" encoding="UTF-8"?>
<hudson.tasks.ArtifactArchiver>
  <artifacts>/target</artifacts>
  <latestOnly>true</latestOnly>
</hudson.tasks.ArtifactArchiver>
'''

    def 'Artifact Archiver XML'() {

        given:
            def delegate = new ArtifactArchiverDelegate()
            delegate.latestOnly = true
            delegate.artifacts = '/target'

        when:
            def theXml = toXml(delegate)
            def xmlDiff = new Diff(theXml, XmlUtil.serialize(artifactXml))

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