package uk.co.accio.jenkins.dsl.publishers.parameterised

import uk.co.accio.jenkins.dsl.publishers.parameterised.config.SubversionRevisionBuildParameters
import uk.co.accio.jenkins.dsl.publishers.parameterised.config.CurrentBuildParameters
import uk.co.accio.jenkins.dsl.publishers.parameterised.config.PredefinedBuildParameters

class BuildTriggerDelegate extends BuildTrigger {

    static String name = 'parameterisedBuildTrigger'

    void projects(String value) {
        this.projects = value
    }

    void withNoParams(value) {
        this.withNoParams = value
    }

    void condition(value) {
        this.condition = value
    }

    void svnRevision(Closure cl) {
        configs << new SubversionRevisionBuildParameters()
    }

    void currentBuildParams(Closure cl) {
         configs << new CurrentBuildParameters()
    }

    void predefinedBuildParams(Closure cl) {
        cl.delegate = new PredefinedBuildParameters()
        cl.resolveStrategy = Closure.DELEGATE_FIRST
        cl()
        configs << cl.delegate
    }
}
