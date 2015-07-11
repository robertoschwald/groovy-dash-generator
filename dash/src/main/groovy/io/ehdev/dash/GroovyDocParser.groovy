package io.ehdev.dash

import io.ehdev.dash.sql.DashSql
import org.apache.commons.io.FileUtils
import org.ccil.cowan.tagsoup.Parser

class GroovyDocParser {

    private final Set<String> entrySet = new HashSet<String>();
    private final DashSql dashSql;
    private final File htmlLocation;
    private final XmlSlurper xmlSlurper;

    public GroovyDocParser(DashSql dashSql, File destination) {
        this.htmlLocation = new File(destination, 'Contents/Resources/Documents/')
        this.dashSql = dashSql;
        this.xmlSlurper = new XmlSlurper(new Parser())
    }

    void processDocRoot(File docRoot) {

        File localHtml = new File(htmlLocation, docRoot.name)
        localHtml.mkdirs()

        FileUtils.copyDirectory(docRoot, localHtml)
        File index = new File(docRoot, 'index-all.html')

        def xml = xmlSlurper.parse(index)

        def terms = xml.depthFirst().findAll { it.name() == 'a' && null != it.@title }
        terms.each { term ->
            processTag(localHtml.name, term)
        }
    }

    private void processTag(String parentDir, term) {
        try {
            processTagThrowsException(parentDir, term)
        } catch (Exception e) {
            println term.@title.text()
        }
    }

    private void processTagThrowsException(String parentDir, term) {
        def aTag = term
        if (!aTag.@title.text().contains(' in ')) {
            return;
        }
        def link = (aTag.@href.text() as String)
        def type = getType(aTag)
        def name = aTag.text()
        if (entrySet.add(link + type + name)) {
            dashSql.insert(name, type, parentDir + '/' + link);
        }
    }

    private ObjectType getType(aTag) {
        String typeName = aTag.@title.text().split(' ').first() as String

        for (ObjectType type : ObjectType.values()) {
            if (type.getValue().equalsIgnoreCase(typeName)) {
                return type
            }
        }
        throw new RuntimeException("Could not find `$typeName`: $aTag")
    }
}
