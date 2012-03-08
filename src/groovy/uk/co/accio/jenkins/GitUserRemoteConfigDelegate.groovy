package uk.co.accio.jenkins

/**
 * <userRemoteConfigs>
      <hudson.plugins.git.UserRemoteConfig>
        <name></name>
        <refspec></refspec>
        <url>git://github.com/springyboing/jenkins-pipeline-builder.git</url>
      </hudson.plugins.git.UserRemoteConfig>
    </userRemoteConfigs>
 */
class GitUserRemoteConfigDelegate  implements Buildable {

    String _topLevelElement = "hudson.plugins.git.UserRemoteConfig"
    String _name
    String _refspec
    String _url

    void name(name) {
        this._name = name
    }
    void refspec(refspec) {
        this._refspec = refspec
    }
    void url(url) {
        this._url = url
    }

    def void build(GroovyObject builder) {
        def obj = {
            "${_topLevelElement}"() {
                name(_name, [:])
                refspec(_refspec, [:])
                url(_url, [:])
            }
        }
        obj.delegate = builder
        obj()
    }
}