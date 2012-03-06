package uk.co.accio.jenkins

class TriggerDelegate {

    TriggerDelegate(){
    }
//
//    def methodMissing(String name, Object args) {
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
//	}
}