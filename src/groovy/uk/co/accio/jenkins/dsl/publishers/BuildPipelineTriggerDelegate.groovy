package uk.co.accio.jenkins.dsl.publishers

/**
 <au.com.centrumsystems.hudson.plugin.buildpipeline.trigger.BuildPipelineTrigger>
      <downstreamProjectNames>MyChildBuild</downstreamProjectNames>
 </au.com.centrumsystems.hudson.plugin.buildpipeline.trigger.BuildPipelineTrigger>
 */
class BuildPipelineTriggerDelegate implements Buildable {

    String topLevelElement = "au.com.centrumsystems.hudson.plugin.buildpipeline.trigger.BuildPipelineTrigger"

    String downstreamProjectNames

    void downstreamProjectNames(downstreamProjectNames) {
        this.downstreamProjectNames = downstreamProjectNames
    }

    def void build(GroovyObject builder) {
        def obj = {
            "${topLevelElement}"() {
                'downstreamProjectNames'(downstreamProjectNames, [:])
            }
        }
        obj.delegate = builder
        obj()
    }
}
