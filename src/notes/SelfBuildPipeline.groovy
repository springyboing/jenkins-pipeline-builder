
builds {

    buildJob("${appName}-Kickoff") {
        desc "Checks for changes and then kicks off the build pipeline"
        disabled true

        scms {
            git {
                userRemoteConfig {
                    url 'git://github.com/springyboing/jenkins-pipeline-builder.git'
                }
                branches {
                    name 'master'
                }
            }
        }
        triggers {
            scm {
                spec '5 * * * *'
            }
        }
        publishers {
            artifactArchiver {
                artifacts '/**'
            }
            buildTrigger {
                childProjects "${appName}-Unit-Test"
            }
        }
    }

    buildJob("${appName}-Unit-Test") {
        desc "Runs unit tests for the ${appName} application"
        disabled true

        builders {
            copyArtifact {
                projectName "${appName}-Unit-Test"
                filter "/**"
            }
            grails {
                targets "test-app unit:spock"
                nonInteractive true
            }
        }
        publishers {
            artifactArchiver {
                artifacts '/**'
            }
            buildTrigger {
                childProjects "${appName}-Package-Plugin"
            }
        }
    }

    buildJob("${appName}-Package-Plugin") {
        desc "Packages the ${appName} plugin"
        disabled true

        builders {
            copyArtifact {
                projectName "${appName}-Unit-Test"
                filter "/**"
            }
            grails {
                targets "package-plugin"
                nonInteractive true
            }
        }
    }
}