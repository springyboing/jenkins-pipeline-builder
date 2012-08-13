package uk.co.accio.jenkins.dsl

import groovy.xml.XmlUtil
import groovy.xml.StreamingMarkupBuilder

class BuildJob implements Buildable {

    String name
    String description
    String props
    Boolean keepDependencies = false
    Boolean canRoam = false
    Boolean disabled = false
    Boolean blockBuildWhenDownstreamBuilding = false
    Boolean blockBuildWhenUpstreamBuilding = false
    Boolean concurrentBuild = false

    def scmDelegate
    def triggerDelegate
    def builderDelegate
    def buildWrapperDelegate
    def publisherDelegate
    def extrasDelegate

    def void build(GroovyObject builder){
        def obj = {
            "project"() {
                'actions'([:])
                description(description, [:])
                'keepDependencies'(keepDependencies, [:])
                if (extrasDelegate) {
                    out << extrasDelegate
                } else {
                    'properties'([:])
                }
                if (scmDelegate) {
                    out << scmDelegate
                } else {
                    'scm'(class: 'hudson.scm.NullSCM')
                }
                'canRoam'(canRoam, [:])
                'disabled'(disabled, [:])
                'blockBuildWhenDownstreamBuilding'(blockBuildWhenDownstreamBuilding, [:])
                'blockBuildWhenUpstreamBuilding'(blockBuildWhenUpstreamBuilding, [:])

                if (triggerDelegate) {
                    out << triggerDelegate
                } else {
                    'triggers'([class: "vector"])
                }

                'concurrentBuild'(concurrentBuild, [:])

                if (builderDelegate) {
                    out << builderDelegate
                } else {
                    'builders'([:])
                }

                if (publisherDelegate) {
                    out << publisherDelegate
                } else {
                    'publishers'([:])
                }
                
                if (buildWrapperDelegate) {
                    out << buildWrapperDelegate
                } else {
                    'buildWrappers'([:])
                }
            }
        }
        obj.delegate = builder
        obj()
    }

    String toBuildConfigXml() {
        def writer = new StringWriter()
        def builder = new StreamingMarkupBuilder().bind {
            mkp.xmlDeclaration()
            out << this
        }
        writer << builder
        return XmlUtil.serialize(writer.toString())
    }
}
