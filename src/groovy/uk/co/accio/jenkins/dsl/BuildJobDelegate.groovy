package uk.co.accio.jenkins.dsl

import uk.co.accio.jenkins.dsl.triggers.TriggerDelegate
import uk.co.accio.jenkins.dsl.scms.ScmDelegate
import uk.co.accio.jenkins.dsl.builders.BuilderDelegate
import uk.co.accio.jenkins.dsl.wrappers.BuilderWrapperDelegate
import uk.co.accio.jenkins.dsl.publishers.PublisherDelegate
import uk.co.accio.jenkins.dsl.other.BatchTask
import uk.co.accio.jenkins.dsl.other.ExtrasDelegate
import uk.co.accio.jenkins.dsl.other.JdkDelegate

class BuildJobDelegate extends BuildJob {

    void name(String name) {
        this.name = name
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

    void extras(Closure cl) {
        cl.delegate = new ExtrasDelegate()
        cl.resolveStrategy = Closure.DELEGATE_FIRST
        cl()

        extrasDelegate = cl.delegate
    }

    void jdk(Closure cl) {
        cl.delegate = new JdkDelegate()
        cl.resolveStrategy = Closure.DELEGATE_FIRST
        cl()

        jdkDelegate = cl.delegate
    }
}