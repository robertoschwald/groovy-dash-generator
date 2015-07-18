package io.ehdev.dash

import io.ehdev.dash.sql.DashSql
import org.junit.Rule
import org.junit.rules.TemporaryFolder
import spock.lang.Specification

class DashSqlTest extends Specification {

    @Rule
    TemporaryFolder temporaryFolder

    def setup() {
        temporaryFolder = new TemporaryFolder()
        temporaryFolder.create()
    }

    def 'create searchItems'() {
        setup:
        def sql = new DashSql(temporaryFolder.newFolder())
        sql.insert('foo', ObjectType.INTERFACE, 'foo')

        when:
        def items = sql.getSearchItems()

        then:
        items.size() > 0
    }
}
