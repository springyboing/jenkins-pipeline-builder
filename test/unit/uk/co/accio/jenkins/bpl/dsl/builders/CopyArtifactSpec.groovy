package uk.co.accio.jenkins.bpl.dsl.builders

import grails.plugin.spock.UnitSpec
import groovy.xml.StreamingMarkupBuilder
import groovy.xml.XmlUtil
import org.custommonkey.xmlunit.DetailedDiff
import org.custommonkey.xmlunit.Diff
import org.custommonkey.xmlunit.Difference
import org.custommonkey.xmlunit.XMLUnit
import uk.co.accio.jenkins.dsl.builders.CopyArtifactDelegate
import uk.co.accio.jenkins.dsl.builders.BuildSelector
import uk.co.accio.jenkins.dsl.builders.SelectorDelegate

class CopyArtifactSpec extends UnitSpec {

    def setupSpec() {
        XMLUnit.setIgnoreWhitespace(true)
        XMLUnit.setNormalizeWhitespace(true)
        XMLUnit.setNormalize(true)
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

    String toXml(object) {
        def writer = new StringWriter()
        def builder = new StreamingMarkupBuilder().bind {
            out << object
        }
        writer << builder
        return XmlUtil.serialize(writer.toString())
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
                      <filter>/**/*</filter>
                      <target>blur</target>
                      <selector class="hudson.plugins.copyartifact.TriggeredBuildSelector">
                        <fallbackToLastSuccessful>true</fallbackToLastSuccessful>
                      </selector>
                    </hudson.plugins.copyartifact.CopyArtifact>'''.stripIndent()

            def theDSL = '''\
                    copyArtifact {
                        projectName 'BlurJob'
                        filter "/**/*"
                        target 'blur'
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

    def dslToXml(String dslText) {
        def delegate
        Script dslScript = new GroovyShell().parse(dslText)
        dslScript.metaClass = createEMC(dslScript.class, {
            ExpandoMetaClass emc ->
                emc.copyArtifact = { Closure cl ->
                    cl.delegate = new CopyArtifactDelegate()
                    cl.resolveStrategy = Closure.DELEGATE_FIRST
                    cl()
                    delegate = cl.delegate
                }
        })
        dslScript.run()

        def writer = new StringWriter()
        def builder = new StreamingMarkupBuilder().bind {
            mkp.xmlDeclaration()
            out << delegate
        }
        writer << builder
        return XmlUtil.serialize(writer.toString())
    }

    static ExpandoMetaClass createEMC(Class clazz, Closure cl) {
        ExpandoMetaClass emc = new ExpandoMetaClass(clazz, false)
        cl(emc)
        emc.initialize()
        return emc
    }
}
