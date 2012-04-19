package uk.co.accio.jenkins.bpl.dsl.builders

import groovy.xml.XmlUtil
import org.custommonkey.xmlunit.Diff
import uk.co.accio.jenkins.dsl.builders.GradleDelegate
import uk.co.accio.jenkins.bpl.dsl.AbstractDslTester

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

        when:
            def theXml = toXml(delegate)
            def xmlDiff = new Diff(theXml, XmlUtil.serialize(gradleXml))

        then:
            xmlDiff.identical()
    }

    def 'Complete Gradle DSL with file'() {

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
            def theDSL = '''\
                    gradle {
                        name 'Default'
                        description 'A'
                        switches 'B'
                        tasks 'C'
                        rootBuildScriptDir 'D'
                        buildFile 'E'
                        useWrapper true
                    }
                    '''.stripIndent()

        when:
            def theXml = dslToXml(theDSL)
            def xmlDiff = new Diff(theXml, XmlUtil.serialize(gradleXml))

        then:
            xmlDiff.identical()
    }

    def 'Minimul Gradle DSL'() {

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
            def theDSL = '''\
                    gradle {
                    }
                    '''.stripIndent()

        when:
            def theXml = dslToXml(theDSL)
            def xmlDiff = new Diff(theXml, XmlUtil.serialize(gradleXml))

        then:
            xmlDiff.identical()
    }
}