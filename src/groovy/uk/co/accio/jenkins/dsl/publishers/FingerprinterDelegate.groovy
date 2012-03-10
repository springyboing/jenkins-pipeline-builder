package uk.co.accio.jenkins.dsl.publishers

/**
    <hudson.tasks.Fingerprinter>
      <targets>target/**</targets>
      <recordBuildArtifacts>true</recordBuildArtifacts>
    </hudson.tasks.Fingerprinter>
 */
class FingerprinterDelegate implements Buildable {

    String topLevelElement = "hudson.tasks.Fingerprinter"

    String targets
    Boolean recordBuildArtifacts = false

    void targets(targets) {
        this.targets = targets
    }
    void recordBuildArtifacts(Boolean recordBuildArtifacts){
        this.recordBuildArtifacts = recordBuildArtifacts
    }

    def void build(GroovyObject builder) {
        def obj = {
            "${topLevelElement}"() {
                'targets'(targets, [:])
                'recordBuildArtifacts'(recordBuildArtifacts, [:])
            }
        }
        obj.delegate = builder
        obj()
    }
}
