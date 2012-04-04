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
	    mavenRepo "http://maven.jenkins-ci.org/service/local/repositories/releases/content/"
//        mavenRepo "http://snapshots.repository.codehaus.org"
//        mavenRepo "http://repository.codehaus.org"
//        mavenRepo "http://download.java.net/maven/2/"
//        mavenRepo "http://repository.jboss.com/maven2/"
    }
    dependencies {
        // specify dependencies here under either 'build', 'compile', 'runtime', 'test' or 'provided' scopes eg.
        
	    build "org.jenkins-ci.main:cli:1.452" 
        compile 'xmlunit:xmlunit:1.3'
    }

    plugins {
        build(":tomcat:$grailsVersion",
              ":release:1.0.1") {
            export = false
        }
        compile ":spock:0.6"
    }
}

jenkins.host = '192.168.1.64'
//jenkins.port = '8888'
//jenkins.protocol = 'http'
//jenkins.path = ''
jenkins.includePlugins = ['extended-choice-parameter', 'AnsiColor']
jenkins.excludePlugins = ['setenv'] // Used to be required...  But must now be included in Jenkins.

