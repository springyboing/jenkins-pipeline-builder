package uk.co.accio.jenkins.bpl.dsl.publishers

import grails.plugin.spock.UnitSpec
import groovy.xml.StreamingMarkupBuilder
import groovy.xml.XmlUtil
import org.custommonkey.xmlunit.Diff
import org.custommonkey.xmlunit.XMLUnit
import uk.co.accio.jenkins.dsl.publishers.BatchTaskInvokerDelegate
import uk.co.accio.jenkins.dsl.publishers.Threshold
import uk.co.accio.jenkins.dsl.publishers.BatchTask

class BatchTaskInvokerSpec extends UnitSpec {

    def setupSpec() {
        XMLUnit.setIgnoreWhitespace(true)
        XMLUnit.setNormalizeWhitespace(true)
        XMLUnit.setNormalize(true)
    }

    def 'Batch Task Invoker XML'() {

        given:
        def batchTaskXml = '''\
                       <?xml version="1.0" encoding="UTF-8"?>
                       <hudson.plugins.batch__task.BatchTaskInvoker>
                          <configs>
                            <hudson.plugins.batch__task.BatchTaskInvoker_-Config>
                              <project>BlurOne</project>
                              <task>TaskOne</task>
                            </hudson.plugins.batch__task.BatchTaskInvoker_-Config>
                            <hudson.plugins.batch__task.BatchTaskInvoker_-Config>
                              <project>BlurTwo</project>
                              <task>TaskTwo</task>
                            </hudson.plugins.batch__task.BatchTaskInvoker_-Config>
                          </configs>
                          <threshold>
                            <name>UNSTABLE</name>
                            <ordinal>1</ordinal>
                            <color>YELLOW</color>
                          </threshold>
                       </hudson.plugins.batch__task.BatchTaskInvoker>'''.stripIndent()


        def delegate = new BatchTaskInvokerDelegate()
        delegate.threshold = Threshold.UNSTABLE
        delegate.batchTasks << new BatchTask(_name: 'TaskOne', _project: 'BlurOne')
        delegate.batchTasks << new BatchTask(_name: 'TaskTwo', _project: 'BlurTwo')

        when:
        def theXml = toXml(delegate)
        def xmlDiff = new Diff(theXml, XmlUtil.serialize(batchTaskXml))

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

    def 'Batch Task Invoker DSL'() {

        given:
        def batchTaskXml = '''\
                       <?xml version="1.0" encoding="UTF-8"?>
                       <hudson.plugins.batch__task.BatchTaskInvoker>
                          <configs>
                            <hudson.plugins.batch__task.BatchTaskInvoker_-Config>
                              <project>BlurOne</project>
                              <task>TaskOne</task>
                            </hudson.plugins.batch__task.BatchTaskInvoker_-Config>
                            <hudson.plugins.batch__task.BatchTaskInvoker_-Config>
                              <project>BlurTwo</project>
                              <task>TaskTwo</task>
                            </hudson.plugins.batch__task.BatchTaskInvoker_-Config>
                          </configs>
                          <threshold>
                            <name>SUCCESS</name>
                            <ordinal>0</ordinal>
                            <color>BLUE</color>
                          </threshold>
                        </hudson.plugins.batch__task.BatchTaskInvoker>'''.stripIndent()

        def theDSL = '''\
                    invokeBatchTasks {
                        task {
                            project 'BlurOne'
                            name 'TaskOne'
                        }
                        task {
                            project 'BlurTwo'
                            name 'TaskTwo'
                        }
                        threshold 'SUCCESS'
                    }'''.stripIndent()

        when:
        def theXml = dslToXml(theDSL)
        def xmlDiff = new Diff(theXml, XmlUtil.serialize(batchTaskXml))

        then:
        xmlDiff.identical()
    }

    def dslToXml(String dslText) {
        def delegate
        Script dslScript = new GroovyShell().parse(dslText)
        dslScript.metaClass = createEMC(dslScript.class, {
            ExpandoMetaClass emc ->
            emc.invokeBatchTasks = { Closure cl ->
                cl.delegate = new BatchTaskInvokerDelegate()
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