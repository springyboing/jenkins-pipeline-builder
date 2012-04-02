package uk.co.accio.jenkins.dsl

import groovy.xml.StreamingMarkupBuilder
import groovy.xml.XmlUtil

class JenkinsBuildsDelegate {

    def defaultsJob
    def buildJobs = []

    void defaults(Closure cl) {
        cl.delegate = new BuildJobDelegate()
        cl.resolveStrategy = Closure.DELEGATE_FIRST
        cl()

        defaultsJob = cl.delegate
        println "Build Defaults:"
    }

    void buildJob(String name, Closure cl) {
        def delegate = new BuildJobDelegate()
        delegate.name = name
        cl.delegate = delegate
        cl.resolveStrategy = Closure.DELEGATE_FIRST
        cl()

        buildJobs << cl.delegate
    }

    static String toBuildConfig(buildJob) {
        def writer = new StringWriter()
        def builder = new StreamingMarkupBuilder().bind {
            mkp.xmlDeclaration()
            out << buildJob
        }
        writer << builder
        return XmlUtil.serialize(writer.toString())
    }
}
