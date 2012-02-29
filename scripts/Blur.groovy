includeTargets << grailsScript("_GrailsClasspath")

target(main: "The description of the script goes here!") {
	depends(classpath)

	hudson.cli.CLI cli = new hudson.cli.CLI(''.toURL())
}

setDefaultTarget(main)
