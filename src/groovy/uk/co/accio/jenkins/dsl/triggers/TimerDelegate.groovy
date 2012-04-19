package uk.co.accio.jenkins.dsl.triggers

/**
 *
 * <?xml version="1.0" encoding="UTF-8"?>
<hudson.triggers.TimerTrigger>
      <spec>5 * * * *</spec>
    </hudson.triggers.TimerTrigger>

 timer {
    spec '5 * * * *'
 }

 */
class TimerDelegate implements Buildable {

    static String name = 'timer'

    String topLevelElement = 'hudson.triggers.TimerTrigger'
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