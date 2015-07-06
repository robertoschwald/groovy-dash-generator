package io.ehdev.dash

import org.apache.tools.ant.Project
import org.apache.tools.ant.types.Path
import org.junit.Rule
import org.junit.rules.TemporaryFolder
import spock.lang.Specification

class ModifiedGroovyDocTest extends Specification {

    @Rule TemporaryFolder temporaryFolder = new TemporaryFolder()

    def setup() {
        temporaryFolder.create()
    }
    def 'something'() {
        setup:
        def project = new Project()
        project.setBasedir(temporaryFolder.newFolder().getAbsolutePath())

        def doc = new ModifiedGroovyDoc()
        doc.setProject(project)
        doc.setPrivate(false)
        doc.setDestdir(new File('/Volumes/Workspace/gradle/subprojects/docs/build/docs/gradle.docset/Contents/Resources/Documents'))
        doc.setSourcepath(new Path(doc.getProject(), '/Volumes/Workspace/gradle/subprojects/core/src/main/groovy'))

        when:
        doc.execute()

        then:
        noExceptionThrown()
    }
}
