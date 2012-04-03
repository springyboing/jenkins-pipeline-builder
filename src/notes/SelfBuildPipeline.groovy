def disablePipeline = false
def targetDir = 'jpb'
def grailsVersion = 'Grails 2.0.1'

builds {

    buildJob("${appName}-Kickoff") {
        desc "Checks for changes and then kicks off the build pipeline"
        disabled disablePipeline

        scms {
            git {
                userRemoteConfig {
                    url 'git://github.com/springyboing/jenkins-pipeline-builder.git'
                }
                branches {
                    name 'master'
                }
                relativeTargetDir targetDir
            }
        }
        triggers {
            scm {
                spec '5 * * * *'
            }
        }
        builders {
            grails {
                name grailsVersion
                targets "compile"
                nonInteractive true
                projectBaseDir targetDir
            }
        }
        publishers {
            artifactArchiver {
                artifacts '**'
            }
            buildTrigger {
                childProjects "${appName}-Unit-Test,${appName}-Docs"
            }
            joinTrigger {
                joinProjects "${appName}-Package-Plugin"
            }
        }
    }

    buildJob("${appName}-Unit-Test") {
        desc "Runs unit tests for the ${appName} application"
        disabled disablePipeline

        builders {
            copyArtifact {
                projectName "${appName}-Kickoff"
            }
            grails {
                name grailsVersion
                targets "test-app unit:spock"
                nonInteractive true
                projectBaseDir targetDir

            }
        }
        publishers {
            artifactArchiver {
                artifacts '**'
            }
        }
    }

    buildJob("${appName}-Docs") {
        desc "Creates docs for the ${appName} plugin"
        disabled disablePipeline

        builders {
            copyArtifact {
                projectName "${appName}-Kickoff"
            }
            grails {
                name grailsVersion
                targets "doc"
                nonInteractive true
                projectBaseDir targetDir
            }
        }
    }

    buildJob("${appName}-Package-Plugin") {
        desc "Packages the ${appName} plugin"
        disabled disablePipeline

        builders {
            copyArtifact {
                projectName "${appName}-Unit-Test"
            }
            grails {
                name grailsVersion
                targets "package-plugin"
                nonInteractive true
                projectBaseDir targetDir
            }
        }
    }
}