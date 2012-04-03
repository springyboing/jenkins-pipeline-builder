package uk.co.accio.jenkins.bpl.dsl.builders

import grails.plugin.spock.UnitSpec
import groovy.xml.StreamingMarkupBuilder
import groovy.xml.XmlUtil
import org.custommonkey.xmlunit.Diff
import org.custommonkey.xmlunit.XMLUnit
import uk.co.accio.jenkins.dsl.builders.GradleDelegate

class GradleSpec extends UnitSpec {

    def setupSpec() {
        XMLUnit.setIgnoreWhitespace(true)
        XMLUnit.setNormalizeWhitespace(true)
        XMLUnit.setNormalize(true)
    }

    def gradleXml = '''\
            <?xml version="1.0" encoding="UTF-8"?>
            '''.stripIndent()

    def 'Gradle Builder XML'() {

        given:
            def delegate = new GradleDelegate()

        when:
            def theXml = toXml(delegate)
            def xmlDiff = new Diff(theXml, XmlUtil.serialize(gradleXml))

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

    def 'Complete Gradle DSL with file'() {

        given:
            def theDSL = '''\
                    gradle {
                        name 'Default'
                        description 'A'
                        switches 'B'
                        tasks 'C'
                        rootBuildScriptDir 'D'
                        buildFile 'E'
                        useWrapper true
                    }
                    '''.stripIndent()

        when:
            def theXml = dslToXml(theDSL)
            def xmlDiff = new Diff(theXml, XmlUtil.serialize(gradleXml))

        then:
            xmlDiff.identical()
    }

    def 'Minimul Gradle DSL'() {

        given:
            def theDSL = '''\
                    gradle {
                    }
                    '''.stripIndent()

        when:
            def theXml = dslToXml(theDSL)
            def xmlDiff = new Diff(theXml, XmlUtil.serialize(gradleXml))

        then:
            xmlDiff.identical()
    }

    def dslToXml(String dslText) {
        def delegate
        Script dslScript = new GroovyShell().parse(dslText)
        dslScript.metaClass = createEMC(dslScript.class, {
            ExpandoMetaClass emc ->
                emc.gradle = { Closure cl ->
                    cl.delegate = new GradleDelegate()
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
