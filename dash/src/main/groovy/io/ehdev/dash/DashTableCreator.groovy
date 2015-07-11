package io.ehdev.dash

import io.ehdev.dash.sql.DashSql
import io.ehdev.dash.sql.SearchItem
import org.ccil.cowan.tagsoup.Parser

class DashTableCreator {

    private final DashSql dashSql
    private final File htmlLocation
    final private XmlSlurper xmlSlurper;

    DashTableCreator(DashSql dashSql, File destDir) {
        this.htmlLocation = new File(destDir, 'Contents/Resources/Documents/')
        this.dashSql = dashSql

        def parser = new Parser()
        xmlSlurper = new XmlSlurper(parser)
    }

    void addTableEntries() {
        filterSearchItems().each { filename, items ->
            def anchors = items.findAll { !it.anchor.isEmpty() }
            if(!anchors.isEmpty()) {
                println "$filename => ${anchors.collect { it.anchor }.join(', ')}"

                def webpage = new File(htmlLocation, filename)
                def page = webpage.text
                anchors.each { item ->

                    def anchorString = "<a\\s+href=(['\"])#(${item.anchor})"
                    page = page.replaceFirst(anchorString, createDashReference(item) + '<a href=$1$2')
                }

                webpage.text = page
            }

        }
    }

    private String createDashReference(SearchItem item) {
        return "<a name=\"//apple_ref/cpp/${item.type}/${item.name}\" class=\"dashAnchor\" />"
    }

    private Map<String, List<SearchItem>> filterSearchItems() {
        def map = new HashMap<String, List<SearchItem>>()
        dashSql.getSearchItems().each { item ->
            if(null == map[item.file]) {
                map[item.file] = []
            }
            map[item.file] << item
        }

        return map;
    }

}
