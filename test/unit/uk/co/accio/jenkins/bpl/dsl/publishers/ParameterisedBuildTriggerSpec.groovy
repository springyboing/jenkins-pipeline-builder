package uk.co.accio.jenkins.bpl.dsl.publishers

import grails.plugin.spock.UnitSpec

import org.custommonkey.xmlunit.Diff
import org.custommonkey.xmlunit.XMLUnit

import groovy.xml.XmlUtil
import uk.co.accio.jenkins.bpl.dsl.AbstractDslTester
import uk.co.accio.jenkins.dsl.publishers.parameterised.BuildTriggerDelegate
import uk.co.accio.jenkins.dsl.publishers.parameterised.BuildTrigger
import uk.co.accio.jenkins.dsl.publishers.parameterised.config.SubversionRevisionBuildParameters
import uk.co.accio.jenkins.dsl.publishers.parameterised.config.CurrentBuildParameters
import uk.co.accio.jenkins.dsl.publishers.parameterised.Condition

class ParameterisedBuildTriggerSpec extends AbstractDslTester {

    Class delegateClass = BuildTriggerDelegate
    String rootName = BuildTriggerDelegate.name

    def 'Parameterised build trigger XML'() {

        given:
        def buildTriggerXml = '''\
            <?xml version="1.0" encoding="UTF-8"?>
            <hudson.plugins.parameterizedtrigger.BuildTrigger>
              <configs>
                <hudson.plugins.parameterizedtrigger.BuildTriggerConfig>
                  <configs>
                    <hudson.plugins.parameterizedtrigger.SubversionRevisionBuildParameters/>
                    <hudson.plugins.parameterizedtrigger.CurrentBuildParameters/>
                  </configs>
                  <projects>Pipe01</projects>
                  <condition>SUCCESS</condition>
                  <triggerWithNoParameters>false</triggerWithNoParameters>
                </hudson.plugins.parameterizedtrigger.BuildTriggerConfig>
              </configs>
            </hudson.plugins.parameterizedtrigger.BuildTrigger>'''.stripIndent()

            def trigger = new BuildTrigger()
            trigger.projects = 'Pipe01'
            trigger.configs << new SubversionRevisionBuildParameters()
            trigger.configs << new CurrentBuildParameters()
            
        when:
            def theXml = toXml(trigger)
            def xmlDiff = new Diff(theXml, XmlUtil.serialize(buildTriggerXml))
            
        then:
            xmlDiff.identical()
    }

     def 'Parameterised build trigger DSL to bean'() {

        given:
            def theDSL = """\
                parameterisedBuildTrigger {
                        projects 'A,B,C'
                        condition 'FAILED'
                        withNoParams true
                        currentBuildParams {}
                        svnRevision {}
                }""".stripIndent()

        when:
            BuildTrigger trigger = dslToObject(theDSL)

        then:
            trigger.projects == 'A,B,C'
            trigger.condition == Condition.FAILED
            trigger.withNoParams == true
            trigger.configs[0] instanceof CurrentBuildParameters
            trigger.configs[1] instanceof SubversionRevisionBuildParameters
           
    }

    def 'Minimal Parameterised build trigger DSL to bean'() {

        given:
            def theDSL = """\
                parameterisedBuildTrigger {
                        projects 'A,B,C'
                }""".stripIndent()

        when:
            BuildTrigger trigger = dslToObject(theDSL)

        then:
            trigger.projects == 'A,B,C'
            trigger.condition == Condition.SUCCESS
            trigger.withNoParams == false
            trigger.configs.isEmpty()
    }
}
