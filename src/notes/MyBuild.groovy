
build {

    buildJob("Albert") {
        desc "My First Desc"

        scms {
            git {
                userRemoteConfig {
                    name ''
                    refspec ''
                    url 'git://github.com/springyboing/jenkins-pipeline-builder.git'
                }
            }
        }
        triggers {
            timer {
                spec '5 * * * *'
            }
            scm {
                spec '5 * * * *'
            }
        }
        builders {
            grails {
                name "(Default)"
                targets "test-app unit:"
                grailsWorkDir ''
                projectWorkDir ''
                projectBaseDir ''
                serverPort ""
                properties ''
                forceUpgrade false
                nonInteractive true
            }
            builders {
                shell {
                    command 'ls -lash'
                }
            }
        }
        publishers {
            artifactArchiver {
                artifacts '/target/**'
            }
            
        }
        buildWrappers {
            portAllocator {
                ports {
                    name 'GRAILS_HTTP_PORT'
                }
            }
        }
    }

    buildJob("Bob") {
        desc "My First Desc"

        scms {
            git {
                userRemoteConfig {
                    name ''
                    refspec ''
                    url 'git://github.com/springyboing/jenkins-pipeline-builder.git'
                }
            }
        }
        triggers {
        }
        builders {
            builders {
                shell {
                    command 'ls -lash'
                }
            }
            grails {
                name "(Default)"
                targets "test-app unit:"
                grailsWorkDir ''
                projectWorkDir ''
                projectBaseDir ''
                serverPort ""
                properties ''
                forceUpgrade false
                nonInteractive true
            }
        }
        publishers {
        }
        buildWrappers {
            portAllocator {
                ports {
                    name 'GRAILS_HTTP_PORT'
                }
            }
        }
    }

    buildJob("Charlie") {
        desc "My First Desc"

        scms {
            git {
                userRemoteConfig {
                    name ''
                    refspec ''
                    url 'git://github.com/springyboing/jenkins-pipeline-builder.git'
                }
            }
        }
        triggers {
        }
        builders {
            grails {
                name "(Default)"
                targets "test-app unit:"
                grailsWorkDir ''
                projectWorkDir ''
                projectBaseDir ''
                serverPort ""
                properties ''
                forceUpgrade false
                nonInteractive true
            }
        }
        publishers {
        }
        buildWrappers {
            portAllocator {
                ports {
                    name 'GRAILS_HTTP_PORT'
                }
            }
        }
    }
}