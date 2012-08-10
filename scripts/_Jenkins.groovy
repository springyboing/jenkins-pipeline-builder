import java.security.KeyPair

includeTargets << grailsScript("_GrailsArgParsing")
includeTargets << grailsScript("_GrailsClasspath")
includeTargets << grailsScript("_GrailsProxy")
includeTargets << grailsScript('_GrailsPackage')

// Defaults...
jenkinsHost = 'localhost'
jenkinsPort = '8080'
jenkinsProtocol = 'http'
jenkinsPath = ''
jenkinsInputStream = System.in
jenkinsOutputStream = System.out
jenkinsErrorStream = System.err
jenkinsArgs = []

target(executeJenkinsCommand: "The description of the script goes here!") {
    depends(configureProxy, classpath, configureJenkinsServer)

    event "StatusUpdate", ["Executing: ${getJenkinsUrl()} ${jenkinsArgs}"]

    runCliCommand(getJenkinsUrl(), jenkinsArgs, jenkinsInputStream, jenkinsOutputStream, jenkinsErrorStream)
}

target(executeJenkinsLogin: "Login to Jenkins server") {
    depends(configureProxy, classpath)

    configureJenkinsServer()

	event "StatusUpdate", ["TODO: Login!!"]
}

target(configureJenkinsServerFromBuildConfig: "Configuring Jenkins Server Location from buildconfig.groovy") {
    jenkinsHost = buildConfig.jenkins.host ? buildConfig.jenkins.host : jenkinsHost
    jenkinsPort = buildConfig.jenkins.port ? buildConfig.jenkins.port : jenkinsPort
    jenkinsProtocol = buildConfig.jenkins.protocol ? buildConfig.jenkins.protocol : jenkinsProtocol
    jenkinsPath = buildConfig.jenkins.path ? buildConfig.jenkins.path : jenkinsPath
}

target(configureJenkinsServerFromCmdArgs: "Configuring Jenkins Server Location from comand line args") {
    if (argsMap.host) {
        jenkinsHost = argsMap.host
    }
    if (argsMap.port) {
        jenkinsPort = argsMap.port
    }
    if (argsMap.protocol) {
        jenkinsProtocol = argsMap.protocol
    }
    if (argsMap.path) {
        jenkinsPath = argsMap.path
    }
}

target(configureJenkinsServer: "Configuring Jenkins Server Location") {
    configureJenkinsServerFromBuildConfig()
    configureJenkinsServerFromCmdArgs()
}

def getJenkinsUrl() {
    return "${jenkinsProtocol}://${jenkinsHost}:${jenkinsPort}${jenkinsPath}".toURL()
}

def runCliCommand(URL rootUrl, List<String> args, InputStream input = System.in, OutputStream output = System.out, OutputStream err = System.err) {

    hudson.cli.CLI cli = new hudson.cli.CLI(rootUrl)
    if (buildConfig.jenkins.pkifile && new File(buildConfig.jenkins.pkifile).exists()) {

        KeyPair keyPair = cli.loadKey(new File(buildConfig.jenkins.pkifile), buildConfig.jenkins.pkifile ?: null)
        if (keyPair) {
            cli.authenticate(keyPair)
        }
    }
    cli.execute(args, input, output, err)
    cli.close()
}
