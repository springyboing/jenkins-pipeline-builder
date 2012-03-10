package uk.co.accio.jenkins.dsl

import groovy.xml.StreamingMarkupBuilder
import groovy.xml.XmlUtil

class BuildConfiguration {

    static void main(String[] args) {
        runJenkinsBuilder(new File("src/notes/MyBuild.groovy"))
    }

    static void runJenkinsBuilder(File dsl) {
        runJenkinsBuilder(dsl.text)
    }

    static void runJenkinsBuilder(String text) {
        Script dslScript = new GroovyShell().parse(text)

        def jkBuild

        dslScript.metaClass = createEMC(dslScript.class, {
            ExpandoMetaClass emc ->
                emc.build = { Closure cl ->
                    cl.delegate = new JkBuildDelegate()
                    cl.resolveStrategy = Closure.DELEGATE_FIRST
                    cl()
                    jkBuild = cl.delegate
                }
        })
        dslScript.run()

        def writer = new StringWriter()
        def builder = new StreamingMarkupBuilder().bind {
            mkp.xmlDeclaration()
            out << jkBuild
        }
        writer << builder
        println  XmlUtil.serialize(writer.toString())

    }
    static ExpandoMetaClass createEMC(Class clazz, Closure cl) {
        ExpandoMetaClass emc = new ExpandoMetaClass(clazz, false)
        cl(emc)
        emc.initialize()
        return emc
    }
}