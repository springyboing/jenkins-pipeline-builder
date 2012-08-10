package uk.co.accio.jenkins.dsl.scms.svn

class Svn implements Buildable {

    static String name = 'svn'

    String topLevelElement = "hudson.scm.SubversionSCM"
    String excludedRegions
    String includedRegions
    String excludedUsers
    String excludedRevprop
    String excludedCommitMessages
    List repoLocations = []

     def void build(GroovyObject builder){
        def obj = {
            "scm"(class: topLevelElement) {
                if (repoLocations) {
                     'locations'([:]) {
                        for (location in repoLocations) {
                            out << location
                        }
                     }
                } else {
                    locations([:])
                }
                excludedRegions(excludedRegions, [:])
                includedRegions(includedRegions, [:])
                excludedUsers(excludedUsers, [:])
                excludedRevprop(excludedRevprop, [:])
                excludedCommitMessages(excludedCommitMessages, [:])
                workspaceUpdater(class: "hudson.scm.subversion.UpdateWithCleanUpdater")
            }
        }
        obj.delegate = builder
        obj()
    }
}
