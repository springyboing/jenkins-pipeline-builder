
build {

    name "Billy"
    desc "My First Desc"

    keepDependencies false
    properties {}
    canRoam true
    disabled false
    blockBuildWhenDownstreamBuilding false
    blockBuildWhenUpstreamBuilding false
    concurrentBuild false

    scms {
        git {
            configVersion 2
//            userRemoteConfigs {
//                "hudson.plugins.git.UserRemoteConfig" {
//                    name
//                    refspec
//                    url "git://github.com/springyboing/jenkins-pipeline-builder.git"
//                }
//            }
//            branches ("hudson.plugins.git.BranchSpec") {
//                name "master"
//            }
//            disableSubmodules false
//            recursiveSubmodules false
//            doGenerateSubmoduleConfigurations false
//            authorOrCommitter false
//            clean false
//            wipeOutWorkspace false
//            pruneBranches false
//            remotePollfalse false
//            buildChooserClass "hudson.plugins.git.util.DefaultBuildChooser"
//            gitTool 'Default'
//            submoduleCfgClass "list"
//            relativeTargetDir ''
//            reference ''
//            excludedRegions ''
//            excludedUsers ''
//            gitConfigName ''
//            gitConfigEmail ''
//            skipTag false
//            includedRegions ''
            scmName 'blur'
        }
    }
    triggers {
//        "hudson.triggers.SCMTrigger"() {
//            spec "5 * * * *"
//        }
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
        copyArtifact {
            projectName: 'BobJob-1330875179638'
            filter
            target
            selectorClass "hudson.plugins.copyartifact.TriggeredBuildSelector"
        }
    }
    publishers {
        artifactArchiver {
            artifacts "/target"
            latestOnly false
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