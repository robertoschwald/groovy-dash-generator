package io.ehdev.dash

import io.ehdev.dash.sql.DashSql
import spock.lang.Specification

class DashSqlTest extends Specification {

    def 'create searchItems'() {
        setup:
        def file = new File('/Volumes/Workspace/gradle/subprojects/docs/build/docs/gradle.docset')
        def sql = new DashSql(file)

        when:
        def items = sql.getSearchItems()

        then:
        items.size() > 0
        items.each {
            println it
        }
    }
}
