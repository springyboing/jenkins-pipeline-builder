
build {

    name "Billy"
    desc "My First Desc"

    scms {
        "hudson.plugins.git.GitSCM" {
            configVersion 2
            userRemoteConfigs {
                "hudson.plugins.git.UserRemoteConfig" {
                    name
                    refspec
                    url "git://github.com/springyboing/jenkins-pipeline-builder.git"
                }
            }
            branches ("hudson.plugins.git.BranchSpec") {
                name "master"
            }
            disableSubmodules false
            recursiveSubmodules false
            doGenerateSubmoduleConfigurations false
            authorOrCommitter false authorOrCommitter
            clean false
            wipeOutWorkspace false
            pruneBranches false
            remotePollfalse
            buildChooser class: "hudson.plugins.git.util.DefaultBuildChooser"
            gitTool 'Default'
            submoduleCfg class="list"
            relativeTargetDir
            reference
            excludedRegions
            excludedUsers
            gitConfigName
            gitConfigEmail
            skipTag false
            includedRegions
            scmName
        }
    }
    triggers {
        "hudson.triggers.SCMTrigger"() {
            spec "5 * * * *"
        }
    }
    builders {
        "com.g2one.hudson.grails.GrailsBuilder" {
            name "(Default)"
            targets "test-app unit:"
            grailsWorkDir
            projectWorkDi
            projectBaseDir
            serverPort "$GRAILS_HTTP_PORT"
            properties [:]
            forceUpgrade false
            nonInteractive true
        }
        "hudson.plugins.copyartifact.CopyArtifact" {
            projectName: 'BobJob-1330875179638'
            filter
            target
            selector "hudson.plugins.copyartifact.TriggeredBuildSelector"
        }
    }
    publishers {
        "hudson.tasks.ArtifactArchiver" {
            artifacts "/target"
            latestOnly false
        }
    }
    buildWrappers {
        "org.jvnet.hudson.plugins.port__allocator.PortAllocator" {
            ports("org.jvnet.hudson.plugins.port__allocator.DefaultPortType") {
                name 'GRAILS_HTTP_PORT'
            }
        }
    }
}