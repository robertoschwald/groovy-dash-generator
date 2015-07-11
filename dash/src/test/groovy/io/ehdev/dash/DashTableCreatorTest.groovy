package io.ehdev.dash

import io.ehdev.dash.sql.DashSql
import spock.lang.Specification

class DashTableCreatorTest extends Specification {

    def 'create searchItems'() {
        setup:
        def file = new File('/Volumes/Workspace/gradle/subprojects/docs/build/docs/gradle.docset')
        def sql = new DashSql(file)
        def creator = new DashTableCreator(sql, file)

        when:
        creator.addTableEntries()

        then:
        noExceptionThrown()
    }
}
