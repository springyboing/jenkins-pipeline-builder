package uk.co.accio.jenkins.dsl.scms

import uk.co.accio.jenkins.dsl.scms.svn.SvnDelegate
import uk.co.accio.jenkins.dsl.scms.git.GitDelegate

class ScmDelegate implements Buildable {

    String topLevelElementClassIfEmpty = "hudson.scm.NullSCM"
    def scmList = []

    void svn(Closure cl) {
        cl.delegate = new SvnDelegate()
        cl.resolveStrategy = Closure.DELEGATE_FIRST
        cl()

        scmList << cl.delegate
    }

    void git(Closure cl) {
        cl.delegate = new GitDelegate()
        cl.resolveStrategy = Closure.DELEGATE_FIRST
        cl()

        scmList << cl.delegate
    }

    def void build(GroovyObject builder) {
        def obj = {
            for (scm in scmList) {
                out << scm
            }
        }
        obj.delegate = builder
        obj()
    }
}