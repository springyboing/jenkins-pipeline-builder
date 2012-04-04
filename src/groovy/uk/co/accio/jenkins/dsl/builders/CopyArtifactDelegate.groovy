package uk.co.accio.jenkins.dsl.builders

/**
 *
 *
 * <dsl.builders>
    <hudson.plugins.copyartifact.CopyArtifact>
      <projectName>BobJob-1330875179638</projectName>
      <filter></filter>
      <target></target>

      <selector class="hudson.plugins.copyartifact.TriggeredBuildSelector">
        <fallbackToLastSuccessful>true</fallbackToLastSuccessful>
      </selector>
 ...    OR
      <selector class="hudson.plugins.copyartifact.ParameterizedBuildSelector">
        <parameterName>BUILD_SELECTOR</parameterName>
      </selector>
 ...    OR
       <selector class="hudson.plugins.copyartifact.StatusBuildSelector">
        <stable>true</stable>
      </selector>
 ...    OR
        <selector class="hudson.plugins.copyartifact.SavedBuildSelector"/>
 ...    OR
      <selector class="hudson.plugins.copyartifact.SpecificBuildSelector">
        <buildNumber>blur</buildNumber>
      </selector>
 ...    OR
      <selector class="hudson.plugins.copyartifact.WorkspaceSelector"/>

    </hudson.plugins.copyartifact.CopyArtifact>
  </dsl.builders>
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
                out << BuildSelector.Triggered
            }
        }
        obj.delegate = builder
        obj()
    }
}

enum BuildSelector implements Buildable {

    // I know theres one missing...  But which one??
    Triggered("hudson.plugins.copyartifact.TriggeredBuildSelector",),
    Parameterized('hudson.plugins.copyartifact.ParameterizedBuildSelector'),
    Status('hudson.plugins.copyartifact.StatusBuildSelector'),
    Saved("hudson.plugins.copyartifact.SavedBuildSelector"),
    Specific('hudson.plugins.copyartifact.SpecificBuildSelector'),
    Workspace('hudson.plugins.copyartifact.WorkspaceSelector')

    String selectorClass

    private BuildSelector(selectorClass) {
        this.selectorClass = selectorClass
    }

    // TODO: Handle all cases of build selector.
    def void build(GroovyObject builder){
        def obj = {
            'selector'([class: selectorClass]) {
                'fallbackToLastSuccessful'(true, [:])
            }
        }
        obj.delegate = builder
        obj()
    }
}