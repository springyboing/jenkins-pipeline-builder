package uk.co.accio.jenkins

class PublisherDelegate implements Buildable {

    String topLevelElement = "publisher"

    def artifactArchiver

    void artifactArchiver(Closure cl) {
        cl.delegate = new ArtifactArchiverDelegate()
        cl.resolveStrategy = Closure.DELEGATE_FIRST
        cl()

        artifactArchiver = cl.delegate
        println "ArtifactArchiver: "
    }

    def void build(GroovyObject builder){
        def obj = {
            "${topLevelElement}"() {
                out << artifactArchiver
            }
        }
        obj.delegate = builder
        obj()
    }

}
