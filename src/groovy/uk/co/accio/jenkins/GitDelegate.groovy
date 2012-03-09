package uk.co.accio.jenkins

class GitDelegate implements Buildable {

    String topLevelElement = 'hudson.plugins.git.GitSCM'
    Integer _configVersion
    String _scmName
    Boolean _disableSubmodules = false
    Boolean _recursiveSubmodules = false
    Boolean _doGenerateSubmoduleConfigurations = false
    Boolean _authorOrCommitter = false
    Boolean _clean = false
    Boolean _wipeOutWorkspace = false
    Boolean _pruneBranches = false
    Boolean _remotePoll = false
    String _buildChooserClass = 'hudson.plugins.git.util.DefaultBuildChooser'
    String _gitTool = 'Default'
    String _submoduleCfgClass = "list"
    String _relativeTargetDir
    String _reference
    String _excludedRegions
    String _excludedUsers
    String _gitConfigName
    String _gitConfigEmail
    Boolean _skipTag = false
    String _includedRegions
    def _branches = []
    def _userRemoteConfigs = []

    void configVersion(Integer configVersion) {
        this._configVersion = configVersion
    }
    void scmName(scmName) {
        this._scmName = scmName
    }

    def void build(GroovyObject builder) {
        def obj = {
            "scm"(class: topLevelElement) {
                configVersion(_configVersion, [:])
                scmName(_scmName, [:])
                userRemoteConfigs(_userRemoteConfigs)
                branches(_branches)
                disableSubmodules(_disableSubmodules)
                recursiveSubmodules(_recursiveSubmodules)
                doGenerateSubmoduleConfigurations(_doGenerateSubmoduleConfigurations)
                authorOrCommitter(_authorOrCommitter)
                clean(_clean)
                wipeOutWorkspace(_wipeOutWorkspace)
                pruneBranches(_pruneBranches)
                remotePoll(_remotePoll)
                buildChooser(class: _buildChooserClass)
                gitTool(_gitTool)
                submoduleCfg(class: _submoduleCfgClass)
                relativeTargetDir(_relativeTargetDir)
                reference(_reference)
                excludedRegions(_excludedRegions)
                excludedUsers(_excludedUsers)
                gitConfigName(_gitConfigName)
                gitConfigEmail(_gitConfigEmail)
                skipTag(_skipTag)
                includedRegions(_includedRegions)
            }
        }
        obj.delegate = builder
        obj()
    }

    void branches(Closure cl) {
        cl.delegate = new GitBranchDelegate()
        cl.resolveStrategy = Closure.DELEGATE_FIRST
        cl()

        _branches << cl.delegate
    }

    void userRemoteConfigs(Closure cl) {
        cl.delegate = new GitUserRemoteConfigDelegate()
        cl.resolveStrategy = Closure.DELEGATE_FIRST
        cl()

        _userRemoteConfigs << cl.delegate
    }
}
