package uk.co.accio.jenkins

/**
 *
 *
 * <builders>
    <hudson.plugins.copyartifact.CopyArtifact>
      <projectName>BobJob-1330875179638</projectName>
      <filter></filter>
      <target></target>
      <selector class="hudson.plugins.copyartifact.TriggeredBuildSelector"/>
    </hudson.plugins.copyartifact.CopyArtifact>
  </builders>
 *
 *
 */
class CopyArtifactDelegate implements Buildable {

    CopyArtifactDelegate(){
    }

    String topLevelElement = 'hudson.plugins.copyartifact.CopyArtifact'
    String projectName
    String filter
    String target
    String selectorClass = 'hudson.plugins.copyartifact.TriggeredBuildSelector'

    void topLevelElement(tle) {
        this.topLevelElement = tle
    }
    void projectName(name) {
        this.projectName = name
    }
    void target(target) {
        this.target = target
    }

    void filter(filter){
        this.filter = filter
    }

    void selectorClass(selector) {
        this.selectorClass = selector
    }

    def void build(GroovyObject builder){
        def obj = {
            "${topLevelElement}"([:]) {
                'projectName'(projectName, [:])
                'filter'(filter, [:])
                'target'(target, [:])
                'selector'(class: selectorClass)
            }
        }
        obj.delegate = builder
        obj()
    }
}