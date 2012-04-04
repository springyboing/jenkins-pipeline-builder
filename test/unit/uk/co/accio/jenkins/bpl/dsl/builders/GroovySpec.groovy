package uk.co.accio.jenkins.bpl.dsl.builders

import grails.plugin.spock.UnitSpec
import groovy.xml.StreamingMarkupBuilder
import groovy.xml.XmlUtil
import org.custommonkey.xmlunit.DetailedDiff
import org.custommonkey.xmlunit.Diff
import org.custommonkey.xmlunit.Difference
import org.custommonkey.xmlunit.XMLUnit
import uk.co.accio.jenkins.dsl.builders.CopyArtifactDelegate
import uk.co.accio.jenkins.dsl.builders.GroovyDelegate

class GroovySpec extends UnitSpec {

    def setupSpec() {
        XMLUnit.setIgnoreWhitespace(true)
        XMLUnit.setNormalizeWhitespace(true)
        XMLUnit.setNormalize(true)
    }

    def groovyXml = '''\
            <?xml version="1.0" encoding="UTF-8"?>
            '''.stripIndent()

    def 'Groovy Builder XML'() {

        given:
            def delegate = new GroovyDelegate()

        when:
            def theXml = toXml(delegate)
            def xmlDiff = new Diff(theXml, XmlUtil.serialize(groovyXml))

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

    def 'Complete Groovy DSL with file'() {

        given:
            def theDSL = '''\
                    groovy {
                        file 'HelloWorld.groovy'
                        name 'Groovy 1.8.2'
                        parameters 'A'
                        scriptParameters 'B'
                        properties 'C'
                        javaOpts 'D'
                        classPath 'E'
                    }
                    '''.stripIndent()

        when:
            def theXml = dslToXml(theDSL)
            def xmlDiff = new Diff(theXml, XmlUtil.serialize(groovyXml))

        then:
            xmlDiff.identical()
    }

    def 'Complete Groovy DSL with inline script'() {

        given:
            def theDSL = '''\
                    groovy {
                        script 'println "HelloWorld"'
                        name 'Groovy 1.8.2'
                        parameters 'A'
                        scriptParameters 'B'
                        properties 'C'
                        javaOpts 'D'
                        classPath 'E'
                    }
                    '''.stripIndent()

        when:
            def theXml = dslToXml(theDSL)
            def xmlDiff = new Diff(theXml, XmlUtil.serialize(groovyXml))

        then:
            xmlDiff.identical()
    }

    def 'Minimul Groovy DSL script'() {

        given:
            def theDSL = '''\
                    groovy {
                        script 'println "HelloWorld"'
                    }
                    '''.stripIndent()

        when:
            def theXml = dslToXml(theDSL)
            def xmlDiff = new Diff(theXml, XmlUtil.serialize(groovyXml))

        then:
            xmlDiff.identical()
    }

    def dslToXml(String dslText) {
        def delegate
        Script dslScript = new GroovyShell().parse(dslText)
        dslScript.metaClass = createEMC(dslScript.class, {
            ExpandoMetaClass emc ->
                emc.groovy = { Closure cl ->
                    cl.delegate = new GroovyDelegate()
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
