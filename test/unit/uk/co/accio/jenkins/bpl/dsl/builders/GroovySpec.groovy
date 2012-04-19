package uk.co.accio.jenkins.bpl.dsl.builders

import groovy.xml.XmlUtil
import org.custommonkey.xmlunit.Diff
import uk.co.accio.jenkins.dsl.builders.GroovyDelegate
import uk.co.accio.jenkins.dsl.builders.Groovy
import uk.co.accio.jenkins.bpl.dsl.AbstractDslTester

class GroovySpec extends AbstractDslTester {

    def setupSpec() {
        delegateClass = GroovyDelegate
        rootName = GroovyDelegate.name
    }

    def 'Groovy Builder XML'() {

        given:
            def groovyXml = '''\
                    <?xml version="1.0" encoding="UTF-8"?>
                    <hudson.plugins.groovy.Groovy>
                          <scriptSource class="hudson.plugins.groovy.StringScriptSource">
                            <command>println new Date()</command>
                          </scriptSource>
                          <groovyName>(Default)</groovyName>
                          <parameters></parameters>
                          <scriptParameters></scriptParameters>
                          <properties></properties>
                          <javaOpts></javaOpts>
                          <classPath></classPath>
                        </hudson.plugins.groovy.Groovy>
                    '''.stripIndent()
            def delegate = new GroovyDelegate()
            delegate._name = '(Default)'
            delegate._script = 'println new Date()'

        when:
            def theXml = toXml(delegate)
            def xmlDiff = new Diff(theXml, XmlUtil.serialize(groovyXml))

        then:
            xmlDiff.identical()
    }

    def 'Complete Groovy DSL with file'() {

        given:
            def theDSL = '''\
                    groovy {
                        file 'HelloWorld.groovy'
                        name 'Groovy 1.8.2'
                        parameters 'A'
                        scriptParameters 'B'
                        properties 'C'
                        javaOpts 'D'
                        classPath 'E'
                    }'''.stripIndent()

        when:
            Groovy groovyObj = dslToObject(theDSL)

        then:
            groovyObj._file == 'HelloWorld.groovy'
            groovyObj._name == 'Groovy 1.8.2'
            groovyObj._parameters == 'A'
            groovyObj._scriptParameters == 'B'
            groovyObj._properties == 'C'
            groovyObj._javaOpts == 'D'
            groovyObj._classPath == 'E'
    }

    def 'Complete Groovy DSL with inline script'() {

        given:
            def theDSL = '''\
                    groovy {
                        script 'println "HelloWorld"'
                        name 'Groovy 1.8.2'
                        parameters 'A'
                        scriptParameters 'B'
                        properties 'C'
                        javaOpts 'D'
                        classPath 'E'
                    }'''.stripIndent()

        when:
            when:
            Groovy groovyObj = dslToObject(theDSL)

        then:
            groovyObj._script == 'println "HelloWorld"'
            groovyObj._name == 'Groovy 1.8.2'
            groovyObj._parameters == 'A'
            groovyObj._scriptParameters == 'B'
            groovyObj._properties == 'C'
            groovyObj._javaOpts == 'D'
            groovyObj._classPath == 'E'
    }

    def 'Minimul Groovy DSL script'() {

        given:
            def theDSL = '''\
                    groovy {
                        script 'println "HelloWorld"'
                    }'''.stripIndent()

        when:
            when:
            Groovy groovyObj = dslToObject(theDSL)

        then:
            groovyObj._script == 'println "HelloWorld"'
            groovyObj._name == '(Default)'
    }
}