package io.ehdev.dash.test

import spock.lang.Specification

class RendererTest extends Specification {

    def 'idk'() {
        setup:
        def ren = new Renderer([new File('/Volumes/Workspace/gradle/subprojects/core/src/main/groovy')], new File('/tmp/foo'))

        when:
        ren.things()

        then:
        noExceptionThrown()
    }
}
