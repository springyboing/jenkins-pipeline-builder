package uk.co.accio.jenkins.dsl.publishers

/**
 * <hudson.tasks.JavadocArchiver>
      <javadocDir>target/**</javadocDir>
      <keepAll>true</keepAll>
    </hudson.tasks.JavadocArchiver>
 */
class JavadocArchiverDelegate implements Buildable {

    String topLevelElement = "hudson.tasks.JavadocArchiver"

    String javadocDir
    Boolean keepAll = false

    void javadocDir(javadocDir) {
        this.javadocDir = javadocDir
    }
    void keepAll(Boolean keepAll){
        this.keepAll = keepAll
    }

    def void build(GroovyObject builder) {
        def obj = {
            "${topLevelElement}"() {
                'javadocDir'(javadocDir, [:])
                'keepAll'(keepAll, [:])
            }
        }
        obj.delegate = builder
        obj()
    }
}
