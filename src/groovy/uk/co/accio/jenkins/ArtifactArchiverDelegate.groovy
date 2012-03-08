package uk.co.accio.jenkins

class ArtifactArchiverDelegate implements Buildable {

    String topLevelElement = "hudson.tasks.ArtifactArchiver"

    String artifacts
    Boolean latestOnly = false

    void artifacts(artifacts) {
        this.artifacts = artifacts
    }
    void latestOnly(Boolean latestOnly){
        this.latestOnly = latestOnly
    }

    def void build(GroovyObject builder) {
        def obj = {
            "${topLevelElement}"() {
                'artifacts'(artifacts)
                'latestOnly'(latestOnly)
            }
        }
        obj.delegate = builder
        obj()
    }
}
