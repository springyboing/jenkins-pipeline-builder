package uk.co.accio.jenkins.bpl.dsl

import grails.plugin.spock.UnitSpec
import groovy.xml.StreamingMarkupBuilder
import groovy.xml.XmlUtil
import org.custommonkey.xmlunit.Diff
import org.custommonkey.xmlunit.XMLUnit
import uk.co.accio.jenkins.dsl.BuildJobDelegate
import uk.co.accio.jenkins.dsl.BuildConfigurator

class BuildConfiguratorSpec extends UnitSpec {

    def 'Simple single buildJob DSL'() {

        given:
            def buildConfigurator = new BuildConfigurator()
            def theDSL = '''\
                builds {
                    buildJob('MyFirstBuild') {
                    }
                }
                '''.stripIndent()

        when:
            buildConfigurator.runJenkinsBuilder(theDSL)

        then:
            buildConfigurator.buildConfig.buildJobs[0].name == 'MyFirstBuild'
    }

    def 'Simple single buildJob DSL using appName variable'() {

            given:
                def buildConfigurator = new BuildConfigurator()
                buildConfigurator.bindVariable('appName', 'donkey')
                def theDSL = '''\
                    builds {
                        buildJob("${appName}") {
                        }
                    }
                    '''.stripIndent()

            when:
                buildConfigurator.runJenkinsBuilder(theDSL)

            then:
                buildConfigurator.buildConfig.buildJobs[0].name == 'donkey'
        }

    
}
