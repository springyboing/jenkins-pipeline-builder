package uk.co.accio.jenkins.dsl

import uk.co.accio.jenkins.dsl.triggers.TriggerDelegate
import uk.co.accio.jenkins.dsl.scms.ScmDelegate
import uk.co.accio.jenkins.dsl.builders.BuilderDelegate
import uk.co.accio.jenkins.dsl.wrappers.BuilderWrapperDelegate
import uk.co.accio.jenkins.dsl.publishers.PublisherDelegate

class BuildJobDelegate implements Buildable {

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

    void name(String name) {
        println "Name: " + name
    }
    void description(String desc) {
        this.description = desc
    }
    void desc(String desc) {
        this.description = desc
    }
    void properties(props) {
        this.props = props
    }
    void keepDependencies(Boolean keepDependencies) {
        this.keepDependencies = keepDependencies
    }
    void canRoam(Boolean canRoam) {
        this.canRoam = canRoam
    }
    void disabled(Boolean disabled) {
        this.disabled = disabled
    }
    void blockBuildWhenDownstreamBuilding(Boolean blockBuildWhenDownstreamBuilding) {
        this.blockBuildWhenDownstreamBuilding = blockBuildWhenDownstreamBuilding
    }
    void blockBuildWhenUpstreamBuilding(Boolean blockBuildWhenUpstreamBuilding) {
        this.blockBuildWhenUpstreamBuilding = blockBuildWhenUpstreamBuilding
    }
    void concurrentBuild(Boolean concurrentBuild) {
        this.concurrentBuild = concurrentBuild
    }

    void scms(Closure cl) {
        cl.delegate = new ScmDelegate()
        cl.resolveStrategy = Closure.DELEGATE_FIRST
        cl()

        scmDelegate = cl.delegate
    }

    void triggers(Closure cl) {
        cl.delegate = new TriggerDelegate()
        cl.resolveStrategy = Closure.DELEGATE_FIRST
        cl()

        triggerDelegate = cl.delegate
    }

    void builders(Closure cl) {
        cl.delegate = new BuilderDelegate()
        cl.resolveStrategy = Closure.DELEGATE_FIRST
        cl()

        builderDelegate = cl.delegate
    }

    void buildWrappers(Closure cl) {
        cl.delegate = new BuilderWrapperDelegate()
        cl.resolveStrategy = Closure.DELEGATE_FIRST
        cl()

        buildWrapperDelegate = cl.delegate
    }

    void publishers(Closure cl) {
        cl.delegate = new PublisherDelegate()
        cl.resolveStrategy = Closure.DELEGATE_FIRST
        cl()

        publisherDelegate = cl.delegate
    }

    def void build(GroovyObject builder){
        def obj = {
            "project"() {
                'actions'([:])
                'keepDependencies'(keepDependencies, [:])
                'properties'(props, [:])
                if (scmDelegate) {
                    out << scmDelegate
                } else {
                    'scm'(class: 'hudson.scm.NullSCM')
                }
                'canRoam'(canRoam, [:])
                'disabled'(disabled, [:])
                'blockBuildWhenDownstreamBuilding'(blockBuildWhenDownstreamBuilding, [:])
                'blockBuildWhenUpstreamBuilding'(blockBuildWhenUpstreamBuilding, [:])
                'concurrentBuild'(concurrentBuild, [:])
                if (triggerDelegate) {
                    out << triggerDelegate
                } else {
                    'triggers'([class: "vector"])
                }
                
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
}
