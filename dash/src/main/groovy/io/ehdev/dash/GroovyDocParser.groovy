package io.ehdev.dash
import groovy.io.FileType
import groovy.sql.Sql
import org.codehaus.groovy.groovydoc.*
import org.codehaus.groovy.tools.groovydoc.GroovyDocTool

class GroovyDocParser {

    void something(File[] docs, File destDir) {
        def docTool = new GroovyDocTool(docs.collect { it.getAbsolutePath() } as String[])

        for (File docDir : docs) {
            String docDirString = docDir.getAbsolutePath()
            def filenames = []
            docDir.eachFileRecurse(FileType.FILES) { file ->
                def path = file.getAbsolutePath()
                if (!path.contains('internal')) {
                    filenames.add(path - docDirString)
                }
            }

            docTool.add(filenames)
        }

        StringBuilder sb = new StringBuilder()
        sb.append('CREATE TABLE searchIndex(id INTEGER PRIMARY KEY, name TEXT, type TEXT, path TEXT);\n')
        sb.append('CREATE UNIQUE INDEX anchor ON searchIndex (name, type, path);\n')

        def rootDoc = docTool.getRootDoc()
        for (GroovyClassDoc classDoc : rootDoc.classes()) {
            String path = classDoc.getFullPathName().replaceFirst('/', '') + '.html'
            String className = classDoc.name()

            String classType = getType(classDoc)
            sb.append("INSERT OR IGNORE INTO searchIndex(name, type, path) VALUES ('$className', '$classType', '$path');\n")

            for (GroovyConstructorDoc constructorDoc : classDoc.constructors()) {
                if (isPublic(constructorDoc)) {
                    String args = ""
                    if (constructorDoc.parameters().size() > 0) {
                        args = constructorDoc.parameters().collect { it.typeName() }.join(',')
                    }
                    String name = "${className}($args)"
                    sb.append("INSERT OR IGNORE INTO searchIndex(name, type, path) VALUES ('$name', 'Constructor', '$path');\n")
                }
            }

            for (GroovyMethodDoc memberDoc : classDoc.methods()) {
                if (isPublic(memberDoc) && !memberDoc.isSynthetic()) {

                    String methodName = memberDoc.name()
                    String shortParams = memberDoc.parameters().collect { it.typeName() }.join(',')

                    String longParams = memberDoc.parameters().collect { GroovyParameter param ->
                        param.isTypeAvailable() ? param.type().qualifiedTypeName() : param.typeName()
                    }.join(', ')

                    String name = "${methodName}($shortParams)"
                    String hrefName = "${methodName}(${longParams})"
                    sb.append("INSERT OR IGNORE INTO searchIndex(name, type, path) VALUES ('$name', 'Method', '$path#$hrefName');\n")
                }
            }
            for (GroovyFieldDoc fieldDoc : classDoc.properties()) {
                if (isPublic(fieldDoc)) {
                    String name = fieldDoc.name()
                    sb.append("INSERT OR IGNORE INTO searchIndex(name, type, path) VALUES ('$name', 'Field', '$path#$name');\n")
                }
            }
        }

        def sqlLite = new File(destDir, 'Contents/Resources/docSet.dsidx')
        sqlLite.parentFile.mkdirs()
        def instance = Sql.newInstance("jdbc:sqlite:${sqlLite.getAbsolutePath()}", "org.sqlite.JDBC")
        println sb.toString().eachLine { line ->
            println line
            instance.execute(line)
        }

    }

    static String getType(GroovyClassDoc groovyClassDoc) {
        if(groovyClassDoc.isInterface()) {
            return "Interface"
        }
        if(groovyClassDoc.isAnnotationType()) {
            return "Annotation"
        }

        return "Class"
    }

    static boolean isPublic(GroovyProgramElementDoc doc) {
        boolean isParentPrivate = false;
        if(null != doc.containingClass()) {
            isParentPrivate = doc.containingClass().isPrivate()
        }
        return doc.public && !isParentPrivate || !(doc.packagePrivate || doc.private);
    }
}
