package io.ehdev.dash.parser

import io.ehdev.dash.ObjectType
import io.ehdev.dash.render.FindFilesToRender
import io.ehdev.dash.sql.DashSql
import org.codehaus.groovy.groovydoc.*
import org.codehaus.groovy.tools.groovydoc.GroovyDocTool
import org.codehaus.groovy.tools.groovydoc.SimpleGroovyParameter

class ClassDocParser {

    private final List<File> sourceRoots;
    private final DashSql dashSql;

    ClassDocParser(List<File> sourceRoots, DashSql dashSql) {
        this.sourceRoots = sourceRoots
        this.dashSql = dashSql
    }

    void parseDocs() {
        def tool = new GroovyDocTool(sourceRoots.collect { it.absolutePath } as String[])
        def filenames = FindFilesToRender.findFilesToProcess(sourceRoots)
        tool.add(filenames)

        for (GroovyClassDoc groovyClassDoc : tool.getRootDoc().classes()) {
            if(groovyClassDoc.isPrivate()) {
                continue
            }
            def filename = 'groovydoc/' + groovyClassDoc.getFullPathName() + '.html'

            forDocClass(groovyClassDoc, ObjectType.CLASS, filename)

            forEachMember(groovyClassDoc.fields(), ObjectType.FIELD, filename)
            forEachMember(groovyClassDoc.enumConstants(), ObjectType.ENUM, filename)
            forEachMember(groovyClassDoc.properties(), ObjectType.PROPERTY, filename)
            forEachAnnotation(groovyClassDoc.annotations(), ObjectType.ANNOTATION, filename)

            forEachExecutable(groovyClassDoc.methods(), ObjectType.METHOD, filename)
            forEachExecutable(groovyClassDoc.constructors(), ObjectType.CONSTRUCTOR, filename)
        }
    }

    private void forEachExecutable(GroovyExecutableMemberDoc[] members, ObjectType property, String filename) {
        for(GroovyExecutableMemberDoc methodDoc: members) {
            if(shouldShowField(methodDoc)) {
                def name = nameForParams(methodDoc)
                dashSql.insert(name, property, "${filename}#${name}")
            }
        }
    }

    private void forEachAnnotation(GroovyAnnotationRef[] annotations, ObjectType property, String filename) {
        for(GroovyAnnotationRef annotationRef: annotations) {
            dashSql.insert(annotationRef.name(), property, "${filename}#${annotationRef.name()}" )
        }
    }

    private void forDocClass(GroovyClassDoc doc, ObjectType property, String filename) {
        dashSql.insert(doc.name(), property, "${filename}")

    }

    private void forEachMember(GroovyMemberDoc[] members, ObjectType property, String filename) {
        for(GroovyMemberDoc enumName: members) {
            if(shouldShowField(enumName)) {
                def name = enumName.name()
                dashSql.insert(name, property, "${filename}#${name}")
            }
        }
    }

    private String nameForParams(GroovyExecutableMemberDoc n) {
        def params = n.parameters().collect { SimpleGroovyParameter param ->
            if(param.isTypeAvailable()) {
                return param.type().qualifiedTypeName()
            } else {
                return param.typeName()
            }
        }
        return n.name() + '(' + params.join(', ') + ')'
    }

    private boolean shouldShowField(GroovyMemberDoc doc) {
        return doc.isPublic()
    }
}
