package uk.co.accio.jenkins.bpl.dsl.publishers

import grails.plugin.spock.UnitSpec
import groovy.xml.StreamingMarkupBuilder
import groovy.xml.XmlUtil
import org.custommonkey.xmlunit.Diff
import org.custommonkey.xmlunit.XMLUnit
import uk.co.accio.jenkins.dsl.publishers.ArtifactArchiverDelegate
import uk.co.accio.jenkins.dsl.publishers.BuildTriggerDelegate
import uk.co.accio.jenkins.dsl.publishers.BuildTriggerThresholdDelegate
import uk.co.accio.jenkins.dsl.publishers.Threshold

class BuildTriggerSpec extends UnitSpec {

    def setupSpec() {
        XMLUnit.setIgnoreWhitespace(true)
        XMLUnit.setNormalizeWhitespace(true)
        XMLUnit.setNormalize(true)
    }

    def 'Child Build XML'() {

        given:
            def delegate = new BuildTriggerDelegate()
            delegate.childProjects = 'My Child Build'
            delegate.threshold = Threshold.SUCCESS
            def buildTriggerXml = '''\
                <?xml version="1.0" encoding="UTF-8"?>
                <hudson.tasks.BuildTrigger>
                  <childProjects>My Child Build</childProjects>
                  <threshold>
                    <name>SUCCESS</name>
                    <ordinal>0</ordinal>
                    <color>BLUE</color>
                  </threshold>
                </hudson.tasks.BuildTrigger>'''.stripIndent()

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

    def 'Full Build Trigger DSL'() {

        given:
            def theDSL = '''\
                buildTrigger {
                    childProjects 'My Child Build'
                    threshold 'UNSTABLE'
                }'''.stripIndent()
            def buildTriggerXml = '''\
                <?xml version="1.0" encoding="UTF-8"?>
                <hudson.tasks.BuildTrigger>
                  <childProjects>My Child Build</childProjects>
                  <threshold>
                    <name>UNSTABLE</name>
                    <ordinal>1</ordinal>
                    <color>YELLOW</color>
                  </threshold>
                </hudson.tasks.BuildTrigger>'''.stripIndent()

        when:
            def theXml = dslToXml(theDSL)
            def xmlDiff = new Diff(theXml, XmlUtil.serialize(buildTriggerXml))

        then:
            xmlDiff.identical()
    }

    def 'Minimal Build Trigger DSL'() {

        given:
            def theDSL = '''\
                buildTrigger {
                    childProjects 'My Child Build'
                }'''.stripIndent()
            def buildTriggerXml = '''\
                <?xml version="1.0" encoding="UTF-8"?>
                <hudson.tasks.BuildTrigger>
                  <childProjects>My Child Build</childProjects>
                  <threshold>
                    <name>SUCCESS</name>
                    <ordinal>0</ordinal>
                    <color>BLUE</color>
                  </threshold>
                </hudson.tasks.BuildTrigger>'''.stripIndent()

        when:
            def theXml = dslToXml(theDSL)
            def xmlDiff = new Diff(theXml, XmlUtil.serialize(buildTriggerXml))

        then:
            xmlDiff.identical()
    }

    def dslToXml(String dslText) {
        def delegate
        Script dslScript = new GroovyShell().parse(dslText)
        dslScript.metaClass = createEMC(dslScript.class, {
            ExpandoMetaClass emc ->
                emc.buildTrigger = { Closure cl ->
                    cl.delegate = new BuildTriggerDelegate()
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