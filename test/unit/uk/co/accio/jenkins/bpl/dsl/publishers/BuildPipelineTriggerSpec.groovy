package uk.co.accio.jenkins.bpl.dsl.publishers

import grails.plugin.spock.UnitSpec
import groovy.xml.StreamingMarkupBuilder
import groovy.xml.XmlUtil
import org.custommonkey.xmlunit.Diff
import org.custommonkey.xmlunit.XMLUnit
import uk.co.accio.jenkins.dsl.publishers.MailerDelegate
import uk.co.accio.jenkins.dsl.publishers.BuildPipelineTriggerDelegate

class BuildPipelineTriggerSpec extends UnitSpec {

    def setupSpec() {
        XMLUnit.setIgnoreWhitespace(true)
        XMLUnit.setNormalizeWhitespace(true)
        XMLUnit.setNormalize(true)
    }


    def buildPipelineTriggerXml = '''\
<?xml version="1.0" encoding="UTF-8"?>
<au.com.centrumsystems.hudson.plugin.buildpipeline.trigger.BuildPipelineTrigger>
  <downstreamProjectNames>MyChildBuild</downstreamProjectNames>
</au.com.centrumsystems.hudson.plugin.buildpipeline.trigger.BuildPipelineTrigger>
'''

    def 'Build Pipeline Trigger XML'() {

        given:
            def delegate = new BuildPipelineTriggerDelegate()
            delegate.downstreamProjectNames = 'MyChildBuild'

        when:
            def theXml = toXml(delegate)
            def xmlDiff = new Diff(theXml, XmlUtil.serialize(buildPipelineTriggerXml))

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