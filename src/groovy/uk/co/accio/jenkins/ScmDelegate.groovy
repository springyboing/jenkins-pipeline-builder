package uk.co.accio.jenkins

class ScmDelegate {

    def scmList = []

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

        scmList << cl.delegate
        println "Git: "
    }
}