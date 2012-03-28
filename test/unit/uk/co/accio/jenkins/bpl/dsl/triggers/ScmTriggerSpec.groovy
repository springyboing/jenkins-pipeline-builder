package uk.co.accio.jenkins.bpl.dsl.triggers

import grails.plugin.spock.UnitSpec
import groovy.xml.StreamingMarkupBuilder
import groovy.xml.XmlUtil
import org.custommonkey.xmlunit.Diff
import org.custommonkey.xmlunit.XMLUnit
import uk.co.accio.jenkins.dsl.triggers.ScmDelegate

class ScmTriggerSpec extends UnitSpec {

    def setupSpec() {
        XMLUnit.setIgnoreWhitespace(true)
        XMLUnit.setNormalizeWhitespace(true)
        XMLUnit.setNormalize(true)
    }

    def 'Scm Trigger XML'() {

        given:
            def delegate = new ScmDelegate()
            delegate.spec = '5 * * * *'
            def scmTriggerXml = '''\
                <?xml version="1.0" encoding="UTF-8"?>
                <hudson.triggers.SCMTrigger>
                  <spec>5 * * * *</spec>
                </hudson.triggers.SCMTrigger>'''.stripIndent()

        when:
            def theXml = toXml(delegate)
            def xmlDiff = new Diff(theXml, XmlUtil.serialize(scmTriggerXml))

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

    def 'Minimal SCM Trigger DSL'() {

        given:
            def theDSL = '''\
                scm {
                    spec '5 * * * *'
                }'''.stripIndent()
            def scmTriggerXml = '''\
                <?xml version="1.0" encoding="UTF-8"?>
                <hudson.triggers.SCMTrigger>
                  <spec>5 * * * *</spec>
                </hudson.triggers.SCMTrigger>'''.stripIndent()
        when:
            def theXml = dslToXml(theDSL)
            def xmlDiff = new Diff(theXml, XmlUtil.serialize(scmTriggerXml))

        then:
            xmlDiff.identical()
    }

    def dslToXml(String dslText) {
        def delegate
        Script dslScript = new GroovyShell().parse(dslText)
        dslScript.metaClass = createEMC(dslScript.class, {
            ExpandoMetaClass emc ->
                emc.scm = { Closure cl ->
                    cl.delegate = new ScmDelegate()
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