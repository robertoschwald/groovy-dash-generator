package io.ehdev.dash.render

import org.junit.Rule
import org.junit.rules.TemporaryFolder
import spock.lang.Specification

class RendererTest extends Specification {

    @Rule
    TemporaryFolder temporaryFolder

    def setup() {
        temporaryFolder = new TemporaryFolder()
        temporaryFolder.create()
    }

    def 'test rendering'() {
        setup:
        def ren = new Renderer([new File('src/main/groovy')], temporaryFolder.newFolder())

        when:
        ren.renderDocs()

        then:
        noExceptionThrown()
    }
}
