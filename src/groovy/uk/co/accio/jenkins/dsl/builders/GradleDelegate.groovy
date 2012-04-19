package uk.co.accio.jenkins.dsl.builders

/**
 gradle {
    name 'Default'
    description ''
    switches ''
    tasks ''
    rootBuildScriptDir ''
    buildFile ''
    useWrapper false
 }

 */
class GradleDelegate extends Gradle {

    static String name = 'gradle'

    void name(name) {
        this._name = name
    }
    void gradleName(name) {
        this._name = name
    }
    void description(description) {
        this._description = description
    }
    void switches(switches) {
        this._switches = switches
    }
    void tasks(tasks) {
        this._tasks = tasks
    }
    void rootBuildScriptDir(rootBuildScriptDir) {
        this._rootBuildScriptDir = rootBuildScriptDir
    }
    void buildFile(buildFile) {
        this._buildFile = buildFile
    }
    void useWrapper(Boolean useWrapper) {
        this._useWrapper = useWrapper
    }
}
