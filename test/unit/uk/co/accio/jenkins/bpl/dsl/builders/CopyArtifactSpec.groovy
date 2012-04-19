package uk.co.accio.jenkins.bpl.dsl.builders

import groovy.xml.XmlUtil
import org.custommonkey.xmlunit.Diff
import uk.co.accio.jenkins.dsl.builders.CopyArtifactDelegate
import uk.co.accio.jenkins.dsl.builders.BuildSelector
import uk.co.accio.jenkins.dsl.builders.SelectorDelegate
import uk.co.accio.jenkins.dsl.builders.Permalink
import uk.co.accio.jenkins.bpl.dsl.AbstractDslTester

class CopyArtifactSpec extends AbstractDslTester {

    def setupSpec() {
        delegateClass = CopyArtifactDelegate
        rootName = CopyArtifactDelegate.name
    }

    def 'Copy from Latest successful build XML'() {

        given:
            def copyArtifactXml = '''\
                <hudson.plugins.copyartifact.CopyArtifact>
                  <projectName>BlurJob</projectName>
                  <filter></filter>
                  <target></target>
                  <selector class="hudson.plugins.copyartifact.StatusBuildSelector">
                    <stable>true</stable>
                  </selector>
                </hudson.plugins.copyartifact.CopyArtifact>'''.stripIndent()

            def delegate = new CopyArtifactDelegate()
            delegate.projectName = 'BlurJob'
            delegate.filter = ''
            delegate.target = ''
            delegate.selectorDelegate = new SelectorDelegate(selector: BuildSelector.Status, stable: true)

        when:
            def theXml = toXml(delegate)
            def xmlDiff = new Diff(theXml, XmlUtil.serialize(copyArtifactXml))

        then:
            xmlDiff.identical()
    }

    def 'Copy from Latest successful build DSL'() {

        given:
            def copyArtifactXml = '''\
                <hudson.plugins.copyartifact.CopyArtifact>
                  <projectName>BlurJob</projectName>
                  <filter>/**/*</filter>
                  <target>blur</target>
                  <selector class="hudson.plugins.copyartifact.StatusBuildSelector">
                    <stable>true</stable>
                  </selector>
                </hudson.plugins.copyartifact.CopyArtifact>'''.stripIndent()

            def theDSL = '''\
                copyArtifact {
                    projectName 'BlurJob'
                    filter "/**/*"
                    target 'blur'
                    when {
                        selector 'Status'
                        stable true
                    }
                }'''.stripIndent()

        when:
            def theXml = dslToXml(theDSL)
            def xmlDiff = new Diff(theXml, XmlUtil.serialize(copyArtifactXml))

        then:
            xmlDiff.identical()
    }

    def 'Copy from Upstream build that triggered this job XML'() {

        given:
            def copyArtifactXml = '''\
                <hudson.plugins.copyartifact.CopyArtifact>
                  <projectName>BlurJob</projectName>
                  <filter></filter>
                  <target></target>
                  <selector class="hudson.plugins.copyartifact.TriggeredBuildSelector">
                    <fallbackToLastSuccessful>true</fallbackToLastSuccessful>
                  </selector>
                </hudson.plugins.copyartifact.CopyArtifact>'''.stripIndent()

            def delegate = new CopyArtifactDelegate()
            delegate.projectName = 'BlurJob'
            delegate.selectorDelegate = new SelectorDelegate(selector: BuildSelector.Triggered, fallbackToLastSuccessful: true)

        when:
            def theXml = toXml(delegate)
            def xmlDiff = new Diff(theXml, XmlUtil.serialize(copyArtifactXml))

        then:
            xmlDiff.identical()
    }

    def 'Copy from Upstream build that triggered this job DSL'() {

        given:
            def copyArtifactXml = '''\
                    <hudson.plugins.copyartifact.CopyArtifact>
                      <projectName>BlurJob</projectName>
                      <filter></filter>
                      <target></target>
                      <selector class="hudson.plugins.copyartifact.TriggeredBuildSelector">
                        <fallbackToLastSuccessful>true</fallbackToLastSuccessful>
                      </selector>
                    </hudson.plugins.copyartifact.CopyArtifact>'''.stripIndent()

            def theDSL = '''\
                    copyArtifact {
                        projectName 'BlurJob'
                        when {
                            selector 'Triggered'
                            fallbackToLastSuccessful true
                        }
                    }'''.stripIndent()

        when:
            def theXml = dslToXml(theDSL)
            def xmlDiff = new Diff(theXml, XmlUtil.serialize(copyArtifactXml))

        then:
            xmlDiff.identical()
    }

    def 'Copy from Latest saved build (marked "keep forever") XML'() {

        given:
            def copyArtifactXml = '''\
                <hudson.plugins.copyartifact.CopyArtifact>
                  <projectName>BlurJob</projectName>
                  <filter></filter>
                  <target></target>
                  <selector class="hudson.plugins.copyartifact.SavedBuildSelector"/>
                </hudson.plugins.copyartifact.CopyArtifact>'''.stripIndent()

            def delegate = new CopyArtifactDelegate()
            delegate.projectName = 'BlurJob'
            delegate.selectorDelegate = new SelectorDelegate(selector: BuildSelector.Saved)

        when:
            def theXml = toXml(delegate)
            def xmlDiff = new Diff(theXml, XmlUtil.serialize(copyArtifactXml))

        then:
            xmlDiff.identical()
    }

    def 'Copy from Latest saved build (marked "keep forever") DSL'() {

        given:
            def copyArtifactXml = '''\
                    <hudson.plugins.copyartifact.CopyArtifact>
                      <projectName>BlurJob</projectName>
                      <filter></filter>
                      <target></target>
                      <selector class="hudson.plugins.copyartifact.SavedBuildSelector"/>
                    </hudson.plugins.copyartifact.CopyArtifact>'''.stripIndent()

            def theDSL = '''\
                    copyArtifact {
                        projectName 'BlurJob'
                        when {
                            selector 'Saved'
                        }
                    }'''.stripIndent()

        when:
            def theXml = dslToXml(theDSL)
            def xmlDiff = new Diff(theXml, XmlUtil.serialize(copyArtifactXml))

        then:
            xmlDiff.identical()
    }

    def 'Copy Specified by permalink XML'() {

        given:
            def copyArtifactXml = '''\
                <hudson.plugins.copyartifact.CopyArtifact>
                  <projectName>BlurJob</projectName>
                  <filter></filter>
                  <target></target>
                  <selector class="hudson.plugins.copyartifact.PermalinkBuildSelector">
                    <id>lastBuild</id>
                  </selector>
                </hudson.plugins.copyartifact.CopyArtifact>'''.stripIndent()

            def delegate = new CopyArtifactDelegate()
            delegate.projectName = 'BlurJob'
            delegate.selectorDelegate = new SelectorDelegate(selector: BuildSelector.Permalink, permalink: Permalink.LAST_BUILD)

        when:
            def theXml = toXml(delegate)
            def xmlDiff = new Diff(theXml, XmlUtil.serialize(copyArtifactXml))

        then:
            xmlDiff.identical()
    }

    def 'Copy Specified by permalink DSL'() {

        given:
            def copyArtifactXml = '''\
                    <hudson.plugins.copyartifact.CopyArtifact>
                      <projectName>BlurJob</projectName>
                      <filter></filter>
                      <target></target>
                      <selector class="hudson.plugins.copyartifact.PermalinkBuildSelector">
                        <id>lastBuild</id>
                      </selector>
                    </hudson.plugins.copyartifact.CopyArtifact>'''.stripIndent()

            def theDSL = '''\
                    copyArtifact {
                        projectName 'BlurJob'
                        when {
                            selector 'Permalink'
                            permalink 'last_build'
                        }
                    }'''.stripIndent()

        when:
            def theXml = dslToXml(theDSL)
            def xmlDiff = new Diff(theXml, XmlUtil.serialize(copyArtifactXml))

        then:
            xmlDiff.identical()
    }

    def 'Copy Specific build XML'() {

        given:
            def copyArtifactXml = '''\
                <hudson.plugins.copyartifact.CopyArtifact>
                  <projectName>BlurJob</projectName>
                  <filter></filter>
                  <target></target>
                  <selector class="hudson.plugins.copyartifact.SpecificBuildSelector">
                    <buildNumber>123</buildNumber>
                  </selector>
                </hudson.plugins.copyartifact.CopyArtifact>'''.stripIndent()

            def delegate = new CopyArtifactDelegate()
            delegate.projectName = 'BlurJob'
            delegate.selectorDelegate = new SelectorDelegate(selector: BuildSelector.Specific, buildNumber: '123')

        when:
            def theXml = toXml(delegate)
            def xmlDiff = new Diff(theXml, XmlUtil.serialize(copyArtifactXml))

        then:
            xmlDiff.identical()
    }

    def 'Copy Specific build DSL'() {

        given:
            def copyArtifactXml = '''\
                    <hudson.plugins.copyartifact.CopyArtifact>
                      <projectName>BlurJob</projectName>
                      <filter></filter>
                      <target></target>
                      <selector class="hudson.plugins.copyartifact.SpecificBuildSelector">
                         <buildNumber>123</buildNumber>
                      </selector>
                    </hudson.plugins.copyartifact.CopyArtifact>'''.stripIndent()

            def theDSL = '''\
                    copyArtifact {
                        projectName 'BlurJob'
                        when {
                            selector 'Specific'
                            buildNumber '123'
                        }
                    }'''.stripIndent()

        when:
            def theXml = dslToXml(theDSL)
            def xmlDiff = new Diff(theXml, XmlUtil.serialize(copyArtifactXml))

        then:
            xmlDiff.identical()
    }

    def 'Copy from WORKSPACE of latest completed build XML'() {

            given:
                def copyArtifactXml = '''\
                    <hudson.plugins.copyartifact.CopyArtifact>
                      <projectName>BlurJob</projectName>
                      <filter></filter>
                      <target></target>
                      <selector class="hudson.plugins.copyartifact.WorkspaceSelector"/>
                    </hudson.plugins.copyartifact.CopyArtifact>'''.stripIndent()

                def delegate = new CopyArtifactDelegate()
                delegate.projectName = 'BlurJob'
                delegate.selectorDelegate = new SelectorDelegate(selector: BuildSelector.Workspace)

            when:
                def theXml = toXml(delegate)
                def xmlDiff = new Diff(theXml, XmlUtil.serialize(copyArtifactXml))

            then:
                xmlDiff.identical()
        }

        def 'Copy from WORKSPACE of latest completed build DSL'() {

            given:
                def copyArtifactXml = '''\
                        <hudson.plugins.copyartifact.CopyArtifact>
                          <projectName>BlurJob</projectName>
                          <filter></filter>
                          <target></target>
                          <selector class="hudson.plugins.copyartifact.WorkspaceSelector"/>
                        </hudson.plugins.copyartifact.CopyArtifact>'''.stripIndent()

                def theDSL = '''\
                        copyArtifact {
                            projectName 'BlurJob'
                            when {
                                selector 'Workspace'
                            }
                        }'''.stripIndent()

            when:
                def theXml = dslToXml(theDSL)
                def xmlDiff = new Diff(theXml, XmlUtil.serialize(copyArtifactXml))

            then:
                xmlDiff.identical()
        }

        def 'Copy Specified by a build parameter XML'() {

            given:
                def copyArtifactXml = '''\
                    <hudson.plugins.copyartifact.CopyArtifact>
                      <projectName>BlurJob</projectName>
                      <filter></filter>
                      <target></target>
                      <selector class="hudson.plugins.copyartifact.ParameterizedBuildSelector">
                        <parameterName>BUILD_SELECTOR</parameterName>
                      </selector>
                    </hudson.plugins.copyartifact.CopyArtifact>'''.stripIndent()

                def delegate = new CopyArtifactDelegate()
                delegate.projectName = 'BlurJob'
                delegate.selectorDelegate = new SelectorDelegate(selector: BuildSelector.Parameterized, parameterName: 'BUILD_SELECTOR')

            when:
                def theXml = toXml(delegate)
                def xmlDiff = new Diff(theXml, XmlUtil.serialize(copyArtifactXml))

            then:
                xmlDiff.identical()
        }

        def 'Copy Specified by a build parameter DSL'() {

            given:
                def copyArtifactXml = '''\
                        <hudson.plugins.copyartifact.CopyArtifact>
                          <projectName>BlurJob</projectName>
                          <filter></filter>
                          <target></target>
                          <selector class="hudson.plugins.copyartifact.ParameterizedBuildSelector">
                            <parameterName>BUILD_SELECTOR</parameterName>
                          </selector>
                        </hudson.plugins.copyartifact.CopyArtifact>'''.stripIndent()

                def theDSL = '''\
                        copyArtifact {
                            projectName 'BlurJob'
                            when {
                                selector 'Parameterized'
                                parameterName 'BUILD_SELECTOR'
                            }
                        }'''.stripIndent()

            when:
                def theXml = dslToXml(theDSL)
                def xmlDiff = new Diff(theXml, XmlUtil.serialize(copyArtifactXml))

            then:
                xmlDiff.identical()
        }

    def 'Minimal Copy from build DSL'() {

        given:
        def copyArtifactXml = '''\
                    <hudson.plugins.copyartifact.CopyArtifact>
                      <projectName>BlurJob</projectName>
                      <filter></filter>
                      <target></target>
                      <selector class="hudson.plugins.copyartifact.StatusBuildSelector">
                        <stable>false</stable>
                      </selector>
                    </hudson.plugins.copyartifact.CopyArtifact>'''.stripIndent()

        def theDSL = '''\
                    copyArtifact {
                        projectName 'BlurJob'
                        when {
                            selector 'Status'
                        }
                    }'''.stripIndent()

        when:
        def theXml = dslToXml(theDSL)
        def xmlDiff = new Diff(theXml, XmlUtil.serialize(copyArtifactXml))

        then:
        xmlDiff.identical()
    }

    def 'Optional with minimal Copy from build DSL'() {

        given:
        def copyArtifactXml = '''\
                    <hudson.plugins.copyartifact.CopyArtifact>
                      <projectName>BlurJob</projectName>
                      <filter></filter>
                      <target></target>
                      <selector class="hudson.plugins.copyartifact.StatusBuildSelector">
                        <stable>false</stable>
                      </selector>
                      <optional>true</optional>
                    </hudson.plugins.copyartifact.CopyArtifact>'''.stripIndent()

        def theDSL = '''\
                    copyArtifact {
                        projectName 'BlurJob'
                        optional true
                        when {
                            selector 'Status'
                        }
                    }'''.stripIndent()

        when:
        def theXml = dslToXml(theDSL)
        def xmlDiff = new Diff(theXml, XmlUtil.serialize(copyArtifactXml))

        then:
        xmlDiff.identical()
    }

    def 'Flatten with minimal Copy from build DSL'() {

        given:
        def copyArtifactXml = '''\
                    <hudson.plugins.copyartifact.CopyArtifact>
                      <projectName>BlurJob</projectName>
                      <filter></filter>
                      <target></target>
                      <selector class="hudson.plugins.copyartifact.StatusBuildSelector">
                        <stable>false</stable>
                      </selector>
                      <flatten>true</flatten>
                    </hudson.plugins.copyartifact.CopyArtifact>'''.stripIndent()

        def theDSL = '''\
                    copyArtifact {
                        projectName 'BlurJob'
                        flatten true
                        when {
                            selector 'Status'
                        }
                    }'''.stripIndent()

        when:
        def theXml = dslToXml(theDSL)
        def xmlDiff = new Diff(theXml, XmlUtil.serialize(copyArtifactXml))

        then:
        xmlDiff.identical()
    }

    def 'Filter with minimal Copy from build DSL'() {

        given:
        def copyArtifactXml = '''\
                    <hudson.plugins.copyartifact.CopyArtifact>
                      <projectName>BlurJob</projectName>
                      <filter>/**/*.xml</filter>
                      <target></target>
                      <selector class="hudson.plugins.copyartifact.StatusBuildSelector">
                        <stable>false</stable>
                      </selector>
                    </hudson.plugins.copyartifact.CopyArtifact>'''.stripIndent()

        def theDSL = '''\
                    copyArtifact {
                        projectName 'BlurJob'
                        filter '/**/*.xml'
                        when {
                            selector 'Status'
                        }
                    }'''.stripIndent()

        when:
        def theXml = dslToXml(theDSL)
        def xmlDiff = new Diff(theXml, XmlUtil.serialize(copyArtifactXml))

        then:
        xmlDiff.identical()
    }

    def 'Target with minimal Copy from build DSL'() {

        given:
        def copyArtifactXml = '''\
                    <hudson.plugins.copyartifact.CopyArtifact>
                      <projectName>BlurJob</projectName>
                      <filter></filter>
                      <target>my-dir</target>
                      <selector class="hudson.plugins.copyartifact.StatusBuildSelector">
                        <stable>false</stable>
                      </selector>
                    </hudson.plugins.copyartifact.CopyArtifact>'''.stripIndent()

        def theDSL = '''\
                    copyArtifact {
                        projectName 'BlurJob'
                        target 'my-dir'
                        when {
                            selector 'Status'
                        }
                    }'''.stripIndent()

        when:
        def theXml = dslToXml(theDSL)
        def xmlDiff = new Diff(theXml, XmlUtil.serialize(copyArtifactXml))

        then:
        xmlDiff.identical()
    }
}