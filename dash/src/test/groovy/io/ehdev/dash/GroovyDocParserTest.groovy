package io.ehdev.dash

import org.junit.Rule
import org.junit.rules.TemporaryFolder
import spock.lang.Specification

class GroovyDocParserTest extends Specification {

    @Rule TemporaryFolder temporaryFolder

    def setup() {
        temporaryFolder = new TemporaryFolder()
        temporaryFolder.create()
    }

    def 'something else'() {
        setup:
        def parser = new GroovyDocParser()
        def folder = temporaryFolder.newFolder()
        println folder.absolutePath

        def file = new File('/Volumes/Workspace/gradle/subprojects/docs/build/docs/gradle.docset')
        def sql = new File(file, 'Contents/Resources/docSet.dsidx')
        if(sql.exists()) {
            sql.delete()
        }

        when:
        parser.something([new File('/Volumes/Workspace/gradle/subprojects/core/src/main/groovy')] as File[], file)

        then:
        noExceptionThrown()
    }
}
