package uk.co.accio.jenkins

class PublisherDelegate implements Buildable {

    String topLevelElement = "hudson.tasks.ArtifactArchiver"

    void artifactArchiver(Closure cl) {
        cl.delegate = new ArtifactArchiverDelegate()
        cl.resolveStrategy = Closure.DELEGATE_FIRST
        cl()

        println "ArtifactArchiver: "
    }

    def void build(GroovyObject builder){
        def obj = {
            "${topLevelElement}"() {
                'targets'(targets)
                'name'(name)
                'grailsWorkDir'(grailsWorkDir)
                'projectWorkDir'(projectWorkDir)
                'projectBaseDir'(projectBaseDir)
                'serverPort'(serverPort)
                'properties'(props)
                'forceUpgrade'(forceUpgrade)
                'nonInteractive'(nonInteractive)
            }
        }
        obj.delegate = builder
        obj()
    }

}
