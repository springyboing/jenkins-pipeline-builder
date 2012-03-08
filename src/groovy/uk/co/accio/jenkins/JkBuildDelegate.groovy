package uk.co.accio.jenkins

class JkBuildDelegate implements Buildable {

    String description
    String props
    Boolean keepDependencies = false
    Boolean canRoam = false
    Boolean disabled = false
    Boolean blockBuildWhenDownstreamBuilding = false
    Boolean blockBuildWhenUpstreamBuilding = false
    Boolean concurrentBuild = false

    def scmDelegate
    def triggerDelegate
    def builderDelegate
    def buildWrapperDelegate
    def publisherDelegate

    void name(String name) {
        println "Name: " + name
    }
    void desc(String desc) {
        this.description = desc
        println "Desc: " + desc
    }
    void properties(props) {
        this.props = props
    }
    void keepDependencies(Boolean keepDependencies) {
        this.keepDependencies = keepDependencies
    }
    void canRoam(Boolean canRoam) {
        this.canRoam = canRoam
    }
    void disabled(Boolean disabled) {
        this.disabled = disabled
    }
    void blockBuildWhenDownstreamBuilding(Boolean blockBuildWhenDownstreamBuilding) {
        this.blockBuildWhenDownstreamBuilding = blockBuildWhenDownstreamBuilding
    }
    void blockBuildWhenUpstreamBuilding(Boolean blockBuildWhenUpstreamBuilding) {
        this.blockBuildWhenUpstreamBuilding = blockBuildWhenUpstreamBuilding
    }
    void concurrentBuild(Boolean concurrentBuild) {
        this.concurrentBuild = concurrentBuild
    }

    void scms(Closure cl) {
        cl.delegate = new ScmDelegate()
        cl.resolveStrategy = Closure.DELEGATE_FIRST
        cl()

        scmDelegate = cl.delegate
        println "Scms: "
    }

    void triggers(Closure cl) {
        cl.delegate = new TriggerDelegate()
        cl.resolveStrategy = Closure.DELEGATE_FIRST
        cl()

        triggerDelegate = cl.delegate
        println "Triggers: "
    }

    void builders(Closure cl) {
        cl.delegate = new BuilderDelegate()
        cl.resolveStrategy = Closure.DELEGATE_FIRST
        cl()

        builderDelegate = cl.delegate
        println "Builders: "
    }

    void buildWrappers(Closure cl) {
        cl.delegate = new BuilderWrapperDelegate()
        cl.resolveStrategy = Closure.DELEGATE_FIRST
        cl()

        buildWrapperDelegate = cl.delegate
        println "BuildWrappers: "
    }

    void publishers(Closure cl) {
        cl.delegate = new PublisherDelegate()
        cl.resolveStrategy = Closure.DELEGATE_FIRST
        cl()

        publisherDelegate = cl.delegate
        println "Publishers: "
    }

    def void build(GroovyObject builder){
        def obj = {
            "project"() {
                'actions'()
                'keepDependencies'(keepDependencies)
                'properties'(props)
                'description'(description)
                'canRoam'(canRoam)
                'disabled'(disabled)
                'blockBuildWhenDownstreamBuilding'(blockBuildWhenDownstreamBuilding)
                'blockBuildWhenUpstreamBuilding'(blockBuildWhenUpstreamBuilding)
                'concurrentBuild'(concurrentBuild)
                'actions'()
                'scm'(class: 'hudson.scm.NullSCM')
                'triggers'(class: 'vector')
                'builders'(){}
                'publishers'(){}
                out << scmDelegate
                out << triggerDelegate
                out << builderDelegate
                out << buildWrapperDelegate
                out << publisherDelegate
            }
        }
        obj.delegate = builder
        obj()
    }
}
