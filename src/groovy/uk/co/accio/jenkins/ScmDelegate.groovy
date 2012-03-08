package uk.co.accio.jenkins

class ScmDelegate implements Buildable {

    def scmList = []
    def gitDelegate

    void svn(Closure cl) {
        cl.delegate = new SvnDelegate()
        cl.resolveStrategy = Closure.DELEGATE_FIRST
        cl()

        scmList << cl.delegate
        println "SVN: "
    }

    void git(Closure cl) {
        cl.delegate = new GitDelegate()
        cl.resolveStrategy = Closure.DELEGATE_FIRST
        cl()

        gitDelegate = cl.delegate
        scmList << cl.delegate
        println "Git: "
    }

    def void build(GroovyObject builder) {
        def obj = {
            "scm" {
                out << gitDelegate
                //out << scmList
            }
        }
        obj.delegate = builder
        obj()
    }
}