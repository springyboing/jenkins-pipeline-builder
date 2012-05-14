package uk.co.accio.jenkins.dsl.builders

class Shell implements Buildable {

    String _topLevelElement = 'hudson.tasks.Shell'
    String command

    def void build(GroovyObject builder){
        def obj = {
            "${_topLevelElement}"([:]) {
                'command'(command, [:])

            }
        }
        obj.delegate = builder
        obj()
    }
}
