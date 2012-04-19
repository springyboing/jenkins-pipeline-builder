package uk.co.accio.jenkins.bpl.dsl

import groovy.xml.StreamingMarkupBuilder
import groovy.xml.XmlUtil
import grails.plugin.spock.UnitSpec
import org.custommonkey.xmlunit.XMLUnit
import spock.lang.Shared

abstract class AbstractDslTester extends UnitSpec {

    @Shared
    Class delegateClass
    @Shared
    String rootName

    def setupSpec() {
        XMLUnit.setIgnoreWhitespace(true)
        XMLUnit.setNormalizeWhitespace(true)
        XMLUnit.setNormalize(true)
    }

    def dslToObject(String dslText) {
        println "Delegate: " + delegateClass
        println "DelegateName: " + delegateClass.name
        return dslToObject(dslText, rootName, delegateClass)
    }

    def dslToObject(dslText, rootName, Class delegateClass) {
        def delegate
        Script dslScript = new GroovyShell().parse(dslText)
        dslScript.metaClass = createEMC(dslScript.class, {
            ExpandoMetaClass emc ->
                emc[rootName] = { Closure cl ->
                    cl.delegate = delegateClass.newInstance()
                    cl.resolveStrategy = Closure.DELEGATE_FIRST
                    cl()
                    delegate = cl.delegate
                }
        })
        dslScript.run()

        return delegate
    }

    def dslToXml(String dslText) {
        def delegate = dslToObject(dslText)

        def writer = new StringWriter()
        def builder = new StreamingMarkupBuilder().bind {
            mkp.xmlDeclaration()
            out << delegate
        }
        writer << builder
        return XmlUtil.serialize(writer.toString())
    }

    ExpandoMetaClass createEMC(Class clazz, Closure cl) {
        ExpandoMetaClass emc = new ExpandoMetaClass(clazz, false)
        cl(emc)
        emc.initialize()
        return emc
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
