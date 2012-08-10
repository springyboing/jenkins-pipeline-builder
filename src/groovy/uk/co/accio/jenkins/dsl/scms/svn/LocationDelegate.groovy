package uk.co.accio.jenkins.dsl.scms.svn

class LocationDelegate extends Location {

    static String name = 'location'

    void remote(remote) {
        this._remote = remote
    }

    void local(local) {
        this._local = local
    }
}

class Location implements Buildable {

    static String topLevelElement = 'hudson.scm.SubversionSCM_-ModuleLocation'

    String _remote
    String _local = '.'

    def void build(GroovyObject builder) {
        def obj = {
            "${topLevelElement}"([:]) {
                'remote'(_remote, [:])
                'local'(_local, [:])
            }
        }
        obj.delegate = builder
        obj()
    }
}
