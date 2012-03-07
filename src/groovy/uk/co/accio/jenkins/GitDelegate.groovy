package uk.co.accio.jenkins

class GitDelegate implements Buildable {

    String topLevelElement = 'hudson.plugins.git.GitSCM'

    def void build(GroovyObject builder) {
        def obj = {
            "${topLevelElement}"() {
                'name'("TODO")
            }
        }
        obj.delegate = builder
        obj()
    }

    def methodMissing(String name, Object args) {

//		if (args.length == 1) {
        //			if (args[0] instanceof Closure) {
        //
        //				args[0].delegate = new RuleDelegate(rule)
        //				args[0].resolveStrategy = Closure.DELEGATE_FIRST
        //
        //				args[0]()
        //
        //				this.configuration.addRule rule
        //			} else {
        //				throw new MissingMethodException(name, this.class, args as Object[])
        //			}
        //		} else {
        //			throw new MissingMethodException(name, this.class, args as Object[])
        //		}
    }
}
