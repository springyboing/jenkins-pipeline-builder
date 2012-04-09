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
 ....   OR
      <selector class="hudson.plugins.copyartifact.PermalinkBuildSelector">
        <id>lastBuild</id>
      </selector>

    </hudson.plugins.copyartifact.CopyArtifact>
  </dsl.builders>
 *
 *
 */
class CopyArtifactDelegate implements Buildable {

    CopyArtifactDelegate() {
    }

    String topLevelElement = 'hudson.plugins.copyartifact.CopyArtifact'
    String projectName
    String filter
    String target
    Boolean flatten = false
    Boolean optional = false

    SelectorDelegate selectorDelegate

    void topLevelElement(tle) {
        this.topLevelElement = tle
    }
    void projectName(name) {
        this.projectName = name
    }
    void target(target) {
        this.target = target
    }
    void from(name) {
        this.projectName = name
    }
    void targetDir(target) {
        this.target = target
    }

    void filter(filter){
        this.filter = filter
    }

    void flatten(Boolean flatten) {
        this.flatten = flatten
    }

    void optional(Boolean optional) {
        this.optional = optional
    }

    void when(Closure cl) {
        cl.delegate = new SelectorDelegate()
        cl.resolveStrategy = Closure.DELEGATE_FIRST
        cl()

        selectorDelegate = cl.delegate
    }

    def void build(GroovyObject builder){
        def obj = {
            "${topLevelElement}"([:]) {
                'projectName'(projectName, [:])
                'filter'(filter, [:])
                'target'(target, [:])
                out << selectorDelegate
                if (flatten) {
                    'flatten'(flatten, [:])
                }
                if (optional) {
                    'optional'(optional, [:])
                }
            }
        }
        obj.delegate = builder
        obj()
    }
}

class SelectorDelegate implements Buildable {

    BuildSelector selector
    Boolean stable = false  // StatusBuildSelector
    Boolean fallbackToLastSuccessful = false // TriggeredBuildSelector
    String parameterName    // ParameterizedBuildSelector
    String buildNumber      // SpecificBuildSelector
    Permalink permalink        // Permalink

    public SelectorDelegate(){}
    public SelectorDelegate(String selector) {
        this.selector = selector as BuildSelector
    }

    void selector(selector) {
        this.selector = selector
    }
    void stable(Boolean stable) {
        this.stable = stable
    }
    void stableOnly(Boolean stable) {
        this.stable = stable
    }
    void fallbackToLastSuccessful(Boolean fallbackToLastSuccessful) {
        this.fallbackToLastSuccessful = fallbackToLastSuccessful
    }
    void parameterName(parameterName) {
        this.parameterName = parameterName
    }
    void buildNumber(buildNumber) {
        this.buildNumber = buildNumber
    }
    void permalink(String permalink) {
        this.permalink = permalink?.toUpperCase() as Permalink
    }

    def void build(GroovyObject builder){
        def obj = {
            'selector'([class: selector.clazz]) {
                switch(selector) {
                    case BuildSelector.Status:
                        'stable'(stable, [:])
                        break
                    case BuildSelector.Triggered:
                        'fallbackToLastSuccessful'(fallbackToLastSuccessful, [:])
                        break
                    case BuildSelector.Permalink:
                        'id'(permalink.status, [:])
                        break
                    case BuildSelector.Specific:
                        'buildNumber'(buildNumber, [:])
                        break
                    case BuildSelector.Parameterized:
                        'parameterName'(parameterName, [:])
                        break
                }
            }
        }
        obj.delegate = builder
        obj()
    }
}

/**
<select class="setting-input dropdownList">
 <option value="0">Latest successful build</option>
 <option value="1">Latest saved build (marked "keep forever")</option>
 <option selected="true" value="2">Upstream build that triggered this job</option>
 <option value="3">Specified by permalink</option>
 <option value="4">Specific build</option>
 <option value="5">Copy from WORKSPACE of latest completed build</option>
 <option value="6">Specified by a build parameter</option>
 </select>

 permalink values:
 <select filldependson="../projectName" fillurl="/job/JenkinsPipelineBuilder-Package-Plugin/descriptorByName/hudson.plugins.copyartifact.PermalinkBuildSelector/fillIdItems" name="_.id" value="" class="setting-input  select "><option value="lastBuild">Last build</option><option value="lastStableBuild">Last stable build</option><option value="lastSuccessfulBuild">Last successful build</option><option value="lastFailedBuild">Last failed build</option><option value="lastUnstableBuild">Last unstable build</option><option value="lastUnsuccessfulBuild">Last unsuccessful build</option></select>

 */
enum BuildSelector {

    Status('Latest successful build', 'hudson.plugins.copyartifact.StatusBuildSelector'), // Default (0)
    Saved('Latest saved build (marked "keep forever")', "hudson.plugins.copyartifact.SavedBuildSelector"), //(1)
    Triggered('Upstream build that triggered this job', "hudson.plugins.copyartifact.TriggeredBuildSelector",), // (2)
    Permalink('Specified by permalink', 'hudson.plugins.copyartifact.PermalinkBuildSelector'),
    Specific('Specific build', 'hudson.plugins.copyartifact.SpecificBuildSelector'),
    Workspace('Copy from WORKSPACE of latest completed build', 'hudson.plugins.copyartifact.WorkspaceSelector'),
    Parameterized('Specified by a build parameter', 'hudson.plugins.copyartifact.ParameterizedBuildSelector')

    String description
    String clazz

    private BuildSelector(description, selectorClass) {
        this.description = description
        this.clazz = selectorClass
    }
}

enum Permalink {

    LAST_BUILD('lastBuild', 'Last build'),
    LAST_STABLE_BUILD('lastStableBuild', 'Last stable build'),
    LAST_SUCCESSFUL_BUILD('lastSuccessfulBuild', 'Last successful build'),
    LAST_FAILED_BUILD('lastFailedBuild','Last failed build'),
    LAST_UNSTABLE_BUILD('lastUnstableBuild','Last unstable build'),
    LAST_UNSUCCESSFUL_BUILD('lastUnsuccessfulBuild','Last unsuccessful build')

    String status
    String description

    private Permalink(status, desc) {
        this.status = status
        this.description = desc
    }
}
