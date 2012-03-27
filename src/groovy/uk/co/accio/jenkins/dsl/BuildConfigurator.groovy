package uk.co.accio.jenkins.dsl

import groovy.xml.StreamingMarkupBuilder
import groovy.xml.XmlUtil

class BuildConfigurator {

    Binding binding = new Binding()
    def buildConfig

    static void main(String[] args) {
        def bc = new BuildConfigurator()
        bc.bindVariable("appName", "Simpsons")
        bc.runJenkinsBuilder(new File("src/notes/MyBuild.groovy"))
        bc.printlnBuildJobsAsSingleXml()
    }

    void runJenkinsBuilder(File dsl) {
        runJenkinsBuilder(dsl.text)
    }

    void bindVariable(name, value) {
        binding.setVariable(name, value)
    }

    void runJenkinsBuilder(String text) {

        Script dslScript = new GroovyShell(binding).parse(text)

        dslScript.metaClass = createEMC(dslScript.class, {
            ExpandoMetaClass emc ->
                emc.builds = { Closure cl ->
                    buildConfig = new JenkinsBuildsDelegate()
                    cl.delegate = buildConfig
                    cl.resolveStrategy = Closure.DELEGATE_FIRST
                    cl()
                }
        })
        dslScript.run()
    }

    void printlnBuildJobsAsSingleXml() {
        println buildJobsAsSingleXml()
    }

    String buildJobsAsSingleXml() {
        def writer = new StringWriter()
        def builder = new StreamingMarkupBuilder().bind {
            mkp.xmlDeclaration()
            'builds'([:]) {
                for (buildJob in buildConfig?.buildJobs) {
                    'build'([class: 'poo']) {
                        out << buildJob
                    }
                }
            }
        }
        writer << builder
        return XmlUtil.serialize(writer.toString())
    }
    
    private ExpandoMetaClass createEMC(Class clazz, Closure cl) {
        ExpandoMetaClass emc = new ExpandoMetaClass(clazz, false)
        cl(emc)
        emc.initialize()
        return emc
    }
}