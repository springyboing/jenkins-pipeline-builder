import hudson.cli.CLI

includeTargets << grailsScript("Init")
includeTargets << grailsScript("_GrailsArgParsing")

target(main: "The description of the script goes here!") {
    depends(classpath, parseArguments)

    println "argsMap: " + argsMap

    runCliCommand('http://33.33.33.10:8080', ['help'])
}

setDefaultTarget(main)


def runCliCommand(String rootUrl, List<String> args, InputStream input = System.in, OutputStream output = System.out, OutputStream err = System.err) {
    def CLI cli = new CLI(rootUrl.toURI().toURL())
    cli.execute(args, input, output, err)
    cli.close()
}