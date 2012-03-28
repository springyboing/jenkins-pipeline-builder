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

    def 'Build Pipeline Trigger XML'() {

        given:
            def delegate = new BuildPipelineTriggerDelegate()
            delegate.downstreamProjectNames = 'MyChildBuild'
            def buildPipelineTriggerXml = '''\
                <?xml version="1.0" encoding="UTF-8"?>
                <au.com.centrumsystems.hudson.plugin.buildpipeline.trigger.BuildPipelineTrigger>
                  <downstreamProjectNames>MyChildBuild</downstreamProjectNames>
                </au.com.centrumsystems.hudson.plugin.buildpipeline.trigger.BuildPipelineTrigger>'''.stripIndent()

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

    def 'Minimal Build Pipeline Trigger DSL'() {

        given:
            def theDSL = '''\
                buildPipelineTrigger {
                    downstreamProjectNames 'My Child Build'
                }'''.stripIndent()
            def buildPipelineTriggerXml = '''\
                <?xml version="1.0" encoding="UTF-8"?>
                <au.com.centrumsystems.hudson.plugin.buildpipeline.trigger.BuildPipelineTrigger>
                    <downstreamProjectNames>My Child Build</downstreamProjectNames>
                </au.com.centrumsystems.hudson.plugin.buildpipeline.trigger.BuildPipelineTrigger>'''.stripIndent()

        when:
            def theXml = dslToXml(theDSL)
            def xmlDiff = new Diff(theXml, XmlUtil.serialize(buildPipelineTriggerXml))

        then:
            xmlDiff.identical()
    }

    def dslToXml(String dslText) {
        def delegate
        Script dslScript = new GroovyShell().parse(dslText)
        dslScript.metaClass = createEMC(dslScript.class, {
            ExpandoMetaClass emc ->
                emc.buildPipelineTrigger = { Closure cl ->
                    cl.delegate = new BuildPipelineTriggerDelegate()
                    cl.resolveStrategy = Closure.DELEGATE_FIRST
                    cl()
                    delegate = cl.delegate
                }
        })
        dslScript.run()

        def writer = new StringWriter()
        def builder = new StreamingMarkupBuilder().bind {
            mkp.xmlDeclaration()
            out << delegate
        }
        writer << builder
        return XmlUtil.serialize(writer.toString())
    }

    static ExpandoMetaClass createEMC(Class clazz, Closure cl) {
        ExpandoMetaClass emc = new ExpandoMetaClass(clazz, false)
        cl(emc)
        emc.initialize()
        return emc
    }
}