package uk.co.accio.jenkins.dsl.publishers

/**
 <hudson.tasks.Mailer>
  <recipients>joe.bloggs@example.com</recipients>
  <dontNotifyEveryUnstableBuild>true</dontNotifyEveryUnstableBuild>
  <sendToIndividuals>true</sendToIndividuals>
</hudson.tasks.Mailer>
 */
class MailerDelegate implements Buildable {

    String topLevelElement = "hudson.tasks.Mailer"

    String recipients
    Boolean dontNotifyEveryUnstableBuild = false
    Boolean sendToIndividuals = false

    void recipients(recipients) {
        this.recipients = recipients
    }
    void dontNotifyEveryUnstableBuild(Boolean dontNotifyEveryUnstableBuild) {
        this.dontNotifyEveryUnstableBuild = dontNotifyEveryUnstableBuild
    }
    void sendToIndividuals(Boolean sendToIndividuals) {
        this.sendToIndividuals = sendToIndividuals
    }

    def void build(GroovyObject builder) {
        def obj = {
            "${topLevelElement}"() {
                'recipients'(recipients, [:])
                'dontNotifyEveryUnstableBuild'(dontNotifyEveryUnstableBuild, [:])
                'sendToIndividuals'(sendToIndividuals, [:])
            }
        }
        obj.delegate = builder
        obj()
    }
}
