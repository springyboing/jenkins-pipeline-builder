
build {

    defaults {

        name = "{app.name} ${build.task}"
        description = "Blur"
        scm {
        }
        triggers {
        }
        builders {
        }
        publishers {
        }
        buildWrappers {
        }
    }

    checkout{}
    unitTest{}
    intergrationTest{}
    functionalTest{}
    war{
        after: ['unitTest', 'intergrationTest', 'functionalTest']
        afterManual: ['deploy'] //Manually Execute Downstream Project
        trigger: 'succeeds' // succeeds/unstable/fails/
    }
    deploy{
        after: war

    }

    co {

        split {
            unitTest{}
            intergrationTest{}
            functionalTest{}
        }
        war {}
        deploy {}
    }



    checkout(freestyle) {

        scm {

        }
        triggers {
            <hudson.triggers.SCMTrigger>
                <spec>5 * * * *</spec>
            </hudson.triggers.SCMTrigger>
            </triggers>
            <concurrentBuild>false</concurrentBuild>
        }
        builders {
            grails(com.g2one.hudson.grails.GrailsBuilder) {
                targets = "test-app unit:"
                name = "(Default)"
                grailsWorkDir =
                projectWorkDir =
                projectBaseDir =
                serverPort = $GRAILS_HTTP_PORT
                properties
                forceUpgrade = false
                nonInteractive = true
            }

        }
        buildWrappers {
            type = DefaultPortType
            ports = 'GRAILS_HTTP_PORT'
        }
    }
}