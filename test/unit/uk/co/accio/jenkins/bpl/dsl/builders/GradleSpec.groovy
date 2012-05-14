package uk.co.accio.jenkins.bpl.dsl.builders

import groovy.xml.XmlUtil
import org.custommonkey.xmlunit.Diff
import uk.co.accio.jenkins.dsl.builders.GradleDelegate
import uk.co.accio.jenkins.bpl.dsl.AbstractDslTester
import uk.co.accio.jenkins.dsl.builders.Gradle

class GradleSpec extends AbstractDslTester {

    def setupSpec() {
        delegateClass = GradleDelegate
        rootName = GradleDelegate.name
    }

    def 'Gradle Builder XML'() {

        given:
            def gradleXml = '''\
                <?xml version="1.0" encoding="UTF-8"?>
                <hudson.plugins.gradle.Gradle>
                      <description>A</description>
                      <switches>B</switches>
                      <tasks>C</tasks>
                      <rootBuildScriptDir>D</rootBuildScriptDir>
                      <buildFile>E</buildFile>
                      <gradleName>(Default)</gradleName>
                      <useWrapper>true</useWrapper>
                </hudson.plugins.gradle.Gradle>'''.stripIndent()
            def delegate = new GradleDelegate()
            delegate._buildFile = 'E'
            delegate._tasks = 'C'
            delegate._name = '(Default)'
            delegate._switches = 'B'
            delegate._description = 'A'
            delegate._rootBuildScriptDir = 'D'
            delegate._useWrapper = true

        when:
            def theXml = toXml(delegate)
            def xmlDiff = new Diff(theXml, XmlUtil.serialize(gradleXml))

        then:
            xmlDiff.identical()
    }

    def 'Complete Gradle DSL with file'() {

        given:
            def theDSL = '''\
                    gradle {
                        name 'Default'
                        description 'A'
                        switches 'B'
                        tasks 'compile'
                        rootBuildScriptDir 'D'
                        buildFile 'build.gradle'
                        useWrapper false
                    }
                    '''.stripIndent()

        when:
            Gradle gradle = dslToObject(theDSL)

        then:
            gradle._useWrapper == false
            gradle._name == 'Default'
            gradle._description == 'A'
            gradle._switches == 'B'
            gradle._buildFile == 'build.gradle'
            gradle._tasks == 'compile'
            gradle._rootBuildScriptDir == 'D'
    }

    def 'Minimul Gradle DSL'() {

        given:
            def theDSL = '''\
                    gradle {
                        tasks 'compile'
                    }
                    '''.stripIndent()

        when:
            Gradle gradle = dslToObject(theDSL)

        then:
            gradle._useWrapper == true
            gradle._buildFile == 'build.gradle'
            gradle._tasks == 'compile'
    }
}