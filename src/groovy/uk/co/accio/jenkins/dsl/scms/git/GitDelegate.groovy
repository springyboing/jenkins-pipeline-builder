package uk.co.accio.jenkins.dsl.scms.git

class GitDelegate implements Buildable {

    static String name = 'git'

    String topLevelElement = 'hudson.plugins.git.GitSCM'
    Integer _configVersion = 2
    String _scmName = ''
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
    String _relativeTargetDir = ''
    String _reference = ''
    String _excludedRegions = ''
    String _excludedUsers = ''
    String _gitConfigName = ''
    String _gitConfigEmail = ''
    Boolean _skipTag = false
    String _includedRegions = ''
    def _branches = []
    def _userRemoteConfigs = []

    void configVersion(Integer configVersion) {
        this._configVersion = configVersion
    }
    void scmName(scmName) {
        this._scmName = scmName
    }

    void disableSubmodules(disableSubmodules) {
        this._disableSubmodules = disableSubmodules
    }
    void recursiveSubmodules(recursiveSubmodules) {
        this._recursiveSubmodules = recursiveSubmodules
    }
    void doGenerateSubmoduleConfigurations(doGenerateSubmoduleConfigurations) {
        this._doGenerateSubmoduleConfigurations = doGenerateSubmoduleConfigurations
    }
    void authorOrCommitter(authorOrCommitter) {
        this._authorOrCommitter = authorOrCommitter
    }
    void clean(clean) {
        this._clean = clean
    }
    void wipeOutWorkspace(wipeOutWorkspace) {
        this._wipeOutWorkspace = wipeOutWorkspace
    }
    void pruneBranches(pruneBranches) {
        this._pruneBranches = pruneBranches
    }
    void remotePoll(remotePoll) {
        this._remotePoll = remotePoll
    }

    void gitTool(gitTool) {
        this._gitTool = gitTool
    }
    void submoduleCfgClass(submoduleCfgClass) {
        this._submoduleCfgClass = submoduleCfgClass
    }
    void relativeTargetDir(relativeTargetDir) {
        this._relativeTargetDir = relativeTargetDir
    }
    void reference(reference) {
        this._reference = reference
    }
    void excludedRegions(excludedRegions) {
        this._excludedRegions = excludedRegions
    }
    void excludedUsers(excludedUsers) {
        this._excludedUsers = excludedUsers
    }
    void gitConfigName(gitConfigName) {
        this._gitConfigName = gitConfigName
    }
    void gitConfigEmail(gitConfigEmail) {
        this._gitConfigEmail = gitConfigEmail
    }
    void skipTag(skipTag) {
        this._skipTag = skipTag
    }
    void includedRegions(includedRegions) {
        this._includedRegions = includedRegions
    }

    def void build(GroovyObject builder) {
        def obj = {
            "scm"(class: topLevelElement) {
                configVersion(_configVersion, [:])

                userRemoteConfigs([:]) {
                    if (_userRemoteConfigs) {
                        out << _userRemoteConfigs
                    }
                }
                branches([:]) {
                    if (_branches) {
                        for (branch in _branches) {
                            out << branch
                        }
                    } else {
                        out << new GitBranchDelegate(_name: '**')
                    }
                }
                
                disableSubmodules(_disableSubmodules, [:])
                recursiveSubmodules(_recursiveSubmodules, [:])
                doGenerateSubmoduleConfigurations(_doGenerateSubmoduleConfigurations, [:])
                authorOrCommitter(_authorOrCommitter, [:])
                clean(_clean, [:])
                wipeOutWorkspace(_wipeOutWorkspace, [:])
                pruneBranches(_pruneBranches, [:])
                remotePoll(_remotePoll, [:])
                buildChooser(class: _buildChooserClass)
                gitTool(_gitTool, [:])
                submoduleCfg(class: _submoduleCfgClass)
                relativeTargetDir(_relativeTargetDir, [:])
                reference(_reference, [:])
                excludedRegions(_excludedRegions, [:])
                excludedUsers(_excludedUsers, [:])
                gitConfigName(_gitConfigName, [:])
                gitConfigEmail(_gitConfigEmail, [:])
                skipTag(_skipTag, [:])
                includedRegions(_includedRegions, [:])
                scmName(_scmName, [:])
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

    void userRemoteConfig(Closure cl) {
        cl.delegate = new GitUserRemoteConfigDelegate()
        cl.resolveStrategy = Closure.DELEGATE_FIRST
        cl()

        _userRemoteConfigs << cl.delegate
    }
}
