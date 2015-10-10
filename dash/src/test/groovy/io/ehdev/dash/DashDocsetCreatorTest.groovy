package io.ehdev.dash

import org.junit.Rule
import org.junit.rules.TemporaryFolder
import spock.lang.Specification

import javax.imageio.stream.ImageInputStream

class DashDocsetCreatorTest extends Specification {
    @Rule
    TemporaryFolder temporaryFolder

    def setup() {
        temporaryFolder = new TemporaryFolder()
        temporaryFolder.create()
    }

    def 'something else'() {
        setup:
        def destination = temporaryFolder.newFolder()

        def dashCreator = new DashDocsetCreator([new File('src/main/groovy')], destination)

        when:
        dashCreator.createDashDocset(null,"")

        then:
        noExceptionThrown()
    }
}
