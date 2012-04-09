package uk.co.accio.jenkins.bpl.dsl.other

import grails.plugin.spock.UnitSpec
import groovy.xml.StreamingMarkupBuilder
import groovy.xml.XmlUtil
import org.custommonkey.xmlunit.Diff
import org.custommonkey.xmlunit.XMLUnit
import uk.co.accio.jenkins.dsl.other.TaskDelegate
import uk.co.accio.jenkins.dsl.other.Task
import uk.co.accio.jenkins.dsl.other.BatchTask
import uk.co.accio.jenkins.dsl.other.BatchTaskDelegate

class BatchTasksSpec extends UnitSpec {

    def setupSpec() {
        XMLUnit.setIgnoreWhitespace(true)
        XMLUnit.setNormalizeWhitespace(true)
        XMLUnit.setNormalize(true)
    }

    def 'Batch Task XML'() {

        given:
            def batchTaskXml = '''\
                <?xml version="1.0" encoding="UTF-8"?>
                <hudson.plugins.batch__task.BatchTask>
                    <name>Current uptime</name>
                    <script>uptime</script>
                </hudson.plugins.batch__task.BatchTask>'''.stripIndent()
        
            def delegate = new Task(_name: 'Current uptime', _script: 'uptime')

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

    def 'Individual Batch Task DSL'() {

        given:
            def batchTaskXml = '''\
                <?xml version="1.0" encoding="UTF-8"?>
                <hudson.plugins.batch__task.BatchTaskProperty>
                    <tasks>
                        <hudson.plugins.batch__task.BatchTask>
                            <name>Current uptime</name>
                            <script>uptime</script>
                        </hudson.plugins.batch__task.BatchTask>
                    </tasks>
                </hudson.plugins.batch__task.BatchTaskProperty>'''.stripIndent()

            def theDSL = '''\
                batchTasks {
                    task {
                        name 'Current uptime'
                        script 'uptime'
                    }
                }'''.stripIndent()

        when:
            def theXml = dslToXml(theDSL)
            def xmlDiff = new Diff(theXml, XmlUtil.serialize(batchTaskXml))

        then:
            xmlDiff.identical()
    }

    def 'Multiple Batch Tasks XML'() {

        given:
            def batchTaskXml = '''\
                <?xml version="1.0" encoding="UTF-8"?>
                <hudson.plugins.batch__task.BatchTaskProperty>
                    <tasks>
                        <hudson.plugins.batch__task.BatchTask>
                            <name>Current uptime</name>
                            <script>uptime</script>
                        </hudson.plugins.batch__task.BatchTask>
                        <hudson.plugins.batch__task.BatchTask>
                            <name>Blur</name>
                            <script>blur</script>
                        </hudson.plugins.batch__task.BatchTask>
                    </tasks>
                </hudson.plugins.batch__task.BatchTaskProperty>'''.stripIndent()

            def delegate = new BatchTask()
            delegate.tasks << new Task(_name: 'Current uptime', _script: 'uptime')
            delegate.tasks << new Task(_name: 'Blur', _script: 'blur')

        when:
            def theXml = toXml(delegate)
            def xmlDiff = new Diff(theXml, XmlUtil.serialize(batchTaskXml))

        then:
            xmlDiff.identical()
    }

    def 'Multiple Batch Tasks DSL'() {

        given:
            def batchTaskXml = '''\
                <?xml version="1.0" encoding="UTF-8"?>
                <hudson.plugins.batch__task.BatchTaskProperty>
                    <tasks>
                        <hudson.plugins.batch__task.BatchTask>
                            <name>Current uptime</name>
                            <script>uptime</script>
                        </hudson.plugins.batch__task.BatchTask>
                        <hudson.plugins.batch__task.BatchTask>
                            <name>Blur</name>
                            <script>blur</script>
                        </hudson.plugins.batch__task.BatchTask>
                    </tasks>
                </hudson.plugins.batch__task.BatchTaskProperty>'''.stripIndent()

            def theDSL = '''\
                batchTasks {
                    task {
                        name 'Current uptime'
                        script 'uptime'
                    }
                    task {
                        name 'Blur'
                        script 'blur'
                    }
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
                emc.batchTasks = { Closure cl ->
                    cl.delegate = new BatchTaskDelegate()
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