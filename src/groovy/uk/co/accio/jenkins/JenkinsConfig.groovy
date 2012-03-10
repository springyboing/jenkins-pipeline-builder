package uk.co.accio.jenkins

class JenkinsConfig {


    String configStarter = '''<?xml version='1.0' encoding='UTF-8'?>
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
  <dsl.builders/>
  <publishers/>
  <buildWrappers/>
</project>'''

    def xml = new XmlParser().parse(configMin)

    void addbuilder(String name, String value) {
       // xml.project[0].dsl.builders.
    }

    void addNode(String name, String value) {
        xml.project[0].appendNode {
            
        }
    }   
}
