package uk.co.accio.jenkins.dsl.scms.svn

/**
 * <scm class="hudson.scm.SubversionSCM">
    <locations>
      <hudson.scm.SubversionSCM_-ModuleLocation>
        <remote>http://vwrebuild.project.internal/repos/vw-configurator-api/trunk</remote>
        <local>.</local>
      </hudson.scm.SubversionSCM_-ModuleLocation>
    </locations>
    <excludedRegions></excludedRegions>
    <includedRegions></includedRegions>
    <excludedUsers></excludedUsers>
    <excludedRevprop>asdas</excludedRevprop>
    <excludedCommitMessages>asdas</excludedCommitMessages>
    <workspaceUpdater class="hudson.scm.subversion.UpdateUpdater"/>
  </scm>
 */
class SvnDelegate extends Svn {

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

    void location(Closure cl) {
        cl.delegate = new LocationDelegate()
        cl.resolveStrategy = Closure.DELEGATE_FIRST
        cl()

        repoLocations << cl.delegate
    }
}
