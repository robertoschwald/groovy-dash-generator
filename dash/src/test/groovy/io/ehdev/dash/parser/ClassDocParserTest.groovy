package io.ehdev.dash.parser

import io.ehdev.dash.sql.DashSql
import org.junit.Rule
import org.junit.rules.TemporaryFolder
import spock.lang.Specification

class ClassDocParserTest extends Specification {

    @Rule
    TemporaryFolder temporaryFolder

    def setup() {
        temporaryFolder = new TemporaryFolder()
        temporaryFolder.create()
    }

    def 'foo'() {
        setup:
        def folder = temporaryFolder.newFolder()
        def sql = new DashSql(folder)
        def parser = new ClassDocParser([new File('src/main/groovy')], sql)

        when:
        parser.parseDocs()

        then:
        noExceptionThrown()
    }
}
