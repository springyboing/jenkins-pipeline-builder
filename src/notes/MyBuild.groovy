
build {

    buildJob("Albert") {
        desc "My First Desc"

        scm {
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

    buildJob("Bob") {
        desc "My First Desc"

        scm {
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

    buildJob("Charlie") {
        desc "My First Desc"

        scm {
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