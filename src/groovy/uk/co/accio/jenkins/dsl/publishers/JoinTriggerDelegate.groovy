package uk.co.accio.jenkins.dsl.publishers

/**
 *
<join.JoinTrigger>
<joinProjects>JenkinsPipelineBuilder-Package-Plugin</joinProjects>
<joinPublishers/>
<evenIfDownstreamUnstable>false</evenIfDownstreamUnstable>
</join.JoinTrigger>
 *
 */
class JoinTriggerDelegate implements Buildable {

    String topLevelElement = "join.JoinTrigger"

    String joinProjects
    Boolean evenIfDownstreamUnstable = false // Trigger even if some downstream projects are unstable

    void joinProjects(joinProjects) {
        this.joinProjects = joinProjects
    }
    
    void evenIfDownstreamUnstable(Boolean evenIfDownstreamUnstable) {
        this.evenIfDownstreamUnstable = evenIfDownstreamUnstable
    }

    def void build(GroovyObject builder) {
        def obj = {
            "${topLevelElement}"() {
                'joinProjects'(joinProjects, [:])
                'joinPublishers'([:])
                'evenIfDownstreamUnstable'(evenIfDownstreamUnstable, [:])
            }
        }
        obj.delegate = builder
        obj()
    }
}

