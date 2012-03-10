package uk.co.accio.jenkins.dsl.scms

import uk.co.accio.jenkins.dsl.scms.svn.SvnDelegate
import uk.co.accio.jenkins.dsl.scms.git.GitDelegate

class ScmDelegate implements Buildable {

    String topLevelElementClassIfEmpty = "hudson.scm.NullSCM"
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
            out << gitDelegate
            //out << scmList
        }
        obj.delegate = builder
        obj()
    }
}