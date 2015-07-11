package io.ehdev.dash

import org.junit.Rule
import org.junit.rules.TemporaryFolder
import spock.lang.Specification

class DashDocsetCreatorTest extends Specification {
    @Rule
    TemporaryFolder temporaryFolder

    def setup() {
        temporaryFolder = new TemporaryFolder()
        temporaryFolder.create()
    }

    def 'something else'() {
        setup:
        def destination = new File('/Volumes/Workspace/gradle/subprojects/docs/build/docs/gradle.docset')
        destination.deleteDir()

        def dashCreator = new DashDocsetCreator([new File('/Volumes/Workspace/gradle/subprojects/docs/build/docs/groovydoc'),
                               new File('/Volumes/Workspace/gradle/subprojects/docs/build/docs/javadoc')], destination)

        when:
        dashCreator.createDashDocset()

        then:
        noExceptionThrown()
    }
}
