package uk.co.accio.jenkins.dsl.publishers

/**
 * <hudson.tasks.junit.JUnitResultArchiver>
      <testResults>target/**</testResults>
      <keepLongStdio>true</keepLongStdio>
      <testDataPublishers/>
    </hudson.tasks.junit.JUnitResultArchiver>
 */
class JUnitResultArchiverDelegate implements Buildable {

    String topLevelElement = "hudson.tasks.junit.JUnitResultArchiver"

    String testResults
    Boolean keepLongStdio = false

    void testResults(testResults) {
        this.testResults = testResults
    }
    void keepLongStdio(Boolean keepLongStdio){
        this.keepLongStdio = keepLongStdio
    }

    def void build(GroovyObject builder) {
        def obj = {
            "${topLevelElement}"() {
                'testResults'(testResults, [:])
                'keepLongStdio'(keepLongStdio, [:])
                'testDataPublishers'([:])
            }
        }
        obj.delegate = builder
        obj()
    }
}
