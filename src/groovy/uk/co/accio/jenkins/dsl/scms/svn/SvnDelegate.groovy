package uk.co.accio.jenkins.dsl.scms.svn

class SvnDelegate implements Buildable {

    String topLevelElement = 'blur.blur.blur.SVN'

    def void build(GroovyObject builder){
        def obj = {
            "${topLevelElement}"() {
                'name'("TODO")
            }
        }
        obj.delegate = builder
        obj()
    }
}
