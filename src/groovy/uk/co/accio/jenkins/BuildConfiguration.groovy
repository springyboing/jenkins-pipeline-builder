package uk.co.accio.jenkins

class BuildConfiguration {

    static void main(String[] args) {
        runArchitectureRules(new File("src/notes/MyBuild.groovy"))
    }

    static void runArchitectureRules(File dsl) {
        Script dslScript = new GroovyShell().parse(dsl.text)

        dslScript.metaClass = createEMC(dslScript.class, {
            ExpandoMetaClass emc ->
                emc.build = { Closure cl ->
                    cl.delegate = new JkBuildDelegate()
                    cl.resolveStrategy = Closure.DELEGATE_FIRST
                    cl()
                }
        })
        dslScript.run()
    }
    static ExpandoMetaClass createEMC(Class clazz, Closure cl) {
        ExpandoMetaClass emc = new ExpandoMetaClass(clazz, false)
        cl(emc)
        emc.initialize()
        return emc
    }
}