package uk.co.accio.jenkins.dsl.triggers

/**
 *
 * <?xml version="1.0" encoding="UTF-8"?>
<hudson.triggers.SCMTrigger>
  <spec>5 * * * *</spec>
</hudson.triggers.SCMTrigger></scm>

 scm {
    spec '5 * * * *'
 }

 */
class ScmDelegate implements Buildable {

    String topLevelElement = 'hudson.triggers.SCMTrigger'
    String spec

    void spec(spec) {
        this.spec = spec
    }

    def void build(GroovyObject builder) {
        def obj = {
            "${topLevelElement}"() {
                'spec'(spec, [:])
            }
        }
        obj.delegate = builder
        obj()
	}
}