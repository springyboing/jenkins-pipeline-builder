package uk.co.accio.jenkins.dsl.scms.svn

/**
 * <scm class="hudson.scm.SubversionSCM">
    <locations/>
    <excludedRegions></excludedRegions>
    <includedRegions></includedRegions>
    <excludedUsers></excludedUsers>
    <excludedRevprop>asdas</excludedRevprop>
    <excludedCommitMessages>asdas</excludedCommitMessages>
    <workspaceUpdater class="hudson.scm.subversion.UpdateUpdater"/>
  </scm>
 */
class SvnDelegate implements Buildable {

    String topLevelElement = "hudson.scm.SubversionSCM"
    String excludedRegions
    String includedRegions
    String excludedUsers
    String excludedRevprop
    String excludedCommitMessages

    void excludedRegions(excludedRegions) {
        this.excludedRegions = excludedRegions
    }
    void includedRegions(includedRegions) {
        this.includedRegions = includedRegions
    }
    void excludedUsers(excludedUsers){
        this.excludedUsers = excludedUsers
    }

    void excludedRevprop(excludedRevprop) {
        this.excludedRevprop = excludedRevprop
    }
    void excludedCommitMessages(excludedCommitMessages) {
        this.excludedCommitMessages = excludedCommitMessages
    }

    def void build(GroovyObject builder){
        def obj = {
            "scm"(class: topLevelElement) {
                locations([:])
                excludedRegions(excludedRegions, [:])
                includedRegions(includedRegions, [:])
                excludedUsers(excludedUsers, [:])
                excludedRevprop(excludedRevprop, [:])
                excludedCommitMessages(excludedCommitMessages, [:])
                workspaceUpdater(class: "hudson.scm.subversion.UpdateUpdater")
            }
        }
        obj.delegate = builder
        obj()
    }
}
