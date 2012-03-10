package uk.co.accio.jenkins.dsl.scms.git

/*
<hudson.plugins.git.BranchSpec>
<name>master</name>
</hudson.plugins.git.BranchSpec>

 */
class GitBranchDelegate implements Buildable {

    String _topLevelElement = "hudson.plugins.git.BranchSpec"
    String _name
    
    void name(name) {
        this._name = name
    }

    def void build(GroovyObject builder) {
        def obj = {
            "${_topLevelElement}"() {
                name(_name, [:])
            }
        }
        obj.delegate = builder
        obj()
    }
}
