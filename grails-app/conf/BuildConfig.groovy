grails.project.class.dir = "target/classes"
grails.project.test.class.dir = "target/test-classes"
grails.project.test.reports.dir = "target/test-reports"
grails.project.target.level = 1.6
//grails.project.war.file = "target/${appName}-${appVersion}.war"

grails.project.dependency.resolution = {
    // inherit Grails' default dependencies
    inherits("global") {
        // uncomment to disable ehcache
        // excludes 'ehcache'
    }
    log "warn" // log level of Ivy resolver, either 'error', 'warn', 'info', 'debug' or 'verbose'
    repositories {
        grailsCentral()
        // uncomment the below to enable remote dependency resolution
        // from public Maven repositories
        mavenCentral()
        //mavenLocal()
        mavenRepo "http://maven.jenkins-ci.org/content/repositories/releases/"
    }
    dependencies {
        // specify dependencies here under either 'build', 'compile', 'runtime', 'test' or 'provided' scopes eg.

        compile "org.jenkins-ci.main:cli:1.476"
        compile 'xmlunit:xmlunit:1.3'
    }
    plugins {
        build(":tomcat:$grailsVersion",
              ":release:2.0.4") {
            export = false
        }
        compile ":spock:0.6"
    }
}

//jenkins.host = 'tim-reddings-mac-mini.local'
jenkins.port = '6500'
//jenkins.protocol = 'http'
//jenkins.path = ''
jenkins.pkifile = '/Users/timredding/.ssh/id_rsa'
jenkins.includePlugins = ['extended-choice-parameter', 'AnsiColor', 'promoted-builds']
jenkins.excludePlugins = ['setenv'] // Used to be required...  But must now be included in Jenkins.

