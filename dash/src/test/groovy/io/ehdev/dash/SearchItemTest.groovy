package io.ehdev.dash

import io.ehdev.dash.sql.SearchItem
import spock.lang.Specification

class SearchItemTest extends Specification {

    def 'file and anchor works'() {
        when:
        def item = new SearchItem('absoluteProjectPath(String)', 'Method', 'api/Project.html#absoluteProjectPath(java.lang.String)')

        then:
        item.file == 'api/Project.html'
        item.anchor == 'absoluteProjectPath(java.lang.String)'
    }

    def 'file and anchor works, when no ancor'() {
        when:
        def item = new SearchItem('absoluteProjectPath(String)', 'Method', 'api/Project.html')

        then:
        item.file == 'api/Project.html'
        item.anchor == ''
    }
}
