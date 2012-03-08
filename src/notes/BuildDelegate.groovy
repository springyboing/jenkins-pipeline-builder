package uk.co.accio.jenkins

class BuildDelegate {

    String configMin = '''<?xml version='1.0' encoding='UTF-8'?>
<project>
  <actions/>
  <keepDependencies>false</keepDependencies>
  <properties/>
  <scm class="hudson.scm.NullSCM"/>
  <canRoam>false</canRoam>
  <disabled>false</disabled>
  <blockBuildWhenDownstreamBuilding>false</blockBuildWhenDownstreamBuilding>
  <blockBuildWhenUpstreamBuilding>false</blockBuildWhenUpstreamBuilding>
  <triggers class="vector"/>
  <concurrentBuild>false</concurrentBuild>
  <builders/>
  <publishers/>
  <buildWrappers/>
</project>'''

    def xml = new XmlParser().parse(configMin)

    void name(String name) {
        xml.project[0].appendNode {
            name("Poopy")
        }
    }

    void desc(String desc) {
    }

    void scms(Closure cl) {
        cl.delegate = new ScmDelegate()
        cl.resolveStrategy = Closure.DELEGATE_FIRST
        cl()
    }

    void triggers(Closure cl) {
        cl.delegate = new TriggerDelegate()
        cl.resolveStrategy = Closure.DELEGATE_FIRST
        cl()
    }

    void builders(Closure cl) {
        cl.delegate = new BuilderDelegate()
        cl.resolveStrategy = Closure.DELEGATE_FIRST
        cl()
    }

    void buildWrappers(Closure cl) {
        cl.delegate = new BuilderWrapperDelegate()
        cl.resolveStrategy = Closure.DELEGATE_FIRST
        cl()
    }

    void publishers(Closure cl) {
        cl.delegate = new PublisherDelegate()
        cl.resolveStrategy = Closure.DELEGATE_FIRST
        cl()
    }
}
