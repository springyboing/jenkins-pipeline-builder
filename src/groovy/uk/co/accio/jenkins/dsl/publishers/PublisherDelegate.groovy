package uk.co.accio.jenkins.dsl.publishers

import uk.co.accio.jenkins.dsl.publishers.parameterised.BuildTriggerDelegate as ParamBuildTriggerDelegate
import uk.co.accio.jenkins.dsl.Raw

class PublisherDelegate implements Buildable {

    String topLevelElement = "publishers"

    def publishers = []

    void artifactArchiver(Closure cl) {
        cl.delegate = new ArtifactArchiverDelegate()
        cl.resolveStrategy = Closure.DELEGATE_FIRST
        cl()

        publishers << cl.delegate
    }

     void buildPipelineTrigger(Closure cl) {
        cl.delegate = new BuildPipelineTriggerDelegate()
        cl.resolveStrategy = Closure.DELEGATE_FIRST
        cl()

        publishers << cl.delegate
    }

    void buildTrigger(Closure cl) {
        cl.delegate = new BuildTriggerDelegate()
        cl.resolveStrategy = Closure.DELEGATE_FIRST
        cl()

        publishers << cl.delegate
    }

    void joinTrigger(Closure cl) {
        cl.delegate = new JoinTriggerDelegate()
        cl.resolveStrategy = Closure.DELEGATE_FIRST
        cl()

        publishers << cl.delegate
    }

    void fingerprinter(Closure cl) {
        cl.delegate = new FingerprinterDelegate()
        cl.resolveStrategy = Closure.DELEGATE_FIRST
        cl()

        publishers << cl.delegate
    }

    void javadocArchiver(Closure cl) {
        cl.delegate = new JavadocArchiverDelegate()
        cl.resolveStrategy = Closure.DELEGATE_FIRST
        cl()

        publishers << cl.delegate
    }

    void junitResultArchiver(Closure cl) {
        cl.delegate = new JUnitResultArchiverDelegate()
        cl.resolveStrategy = Closure.DELEGATE_FIRST
        cl()

        publishers << cl.delegate
    }

    void mailer(Closure cl) {
        cl.delegate = new MailerDelegate()
        cl.resolveStrategy = Closure.DELEGATE_FIRST
        cl()

        publishers << cl.delegate
    }

    void invokeBatchTasks(Closure cl) {
        cl.delegate = new BatchTaskInvokerDelegate()
        cl.resolveStrategy = Closure.DELEGATE_FIRST
        cl()

        publishers << cl.delegate
    }

    void parameterisedBuildTrigger(Closure cl) {
        cl.delegate = new ParamBuildTriggerDelegate()
        cl.resolveStrategy = Closure.DELEGATE_FIRST
        cl()

        publishers << cl.delegate
    }

    void raw(String raw) {
        publishers << new Raw(value: raw)
    }

    def void build(GroovyObject builder){
        def obj = {
            "${topLevelElement}"() {
                for (publish in publishers) {
                    out << publish
                }
            }
        }
        obj.delegate = builder
        obj()
    }

}
