package io.ehdev.dash.test

import groovy.io.FileType
import org.codehaus.groovy.tools.groovydoc.ClasspathResourceManager
import org.codehaus.groovy.tools.groovydoc.FileOutputTool
import org.codehaus.groovy.tools.groovydoc.GroovyDocTool
import org.codehaus.groovy.tools.groovydoc.LinkArgument
import org.codehaus.groovy.tools.groovydoc.OutputTool

class Renderer {
    final private List<File> sourceSets;
    final private File dest
    Renderer(List<File> sourceSets, File dest) {
        this.sourceSets = sourceSets
        this.dest = dest
    }

    public void things() {
        def paths = sourceSets.collect { it.getAbsolutePath() }

        def properties = new Properties()
        properties.setProperty('packagenames', '**.*')
        properties.setProperty('use', 'true')

        def tool = new GroovyDocTool(new ClasspathResourceManager(), paths as String[], [] as String[], [] as String[], ['rootdir/classDocName.html'] as String[], getLinkArguments(), properties)
        def files = []
        for(File file: sourceSets) {
            def path = file.absolutePath
            if(file.isDirectory() && !path.endsWith('/')) {
                path += '/'
            }
            file.eachFileRecurse(FileType.FILES) {
                if(!it.absolutePath.contains('/internal/')) {
                    files << it.absolutePath - path
                }
            }
        }

        tool.add(files)
        OutputTool outputTool = new FileOutputTool()
        tool.renderToOutput(outputTool, dest.getAbsolutePath())
        new File(dest, 'stylesheet.css').text = getClass().getResource('/rootdir/stylesheet.css').text
    }

    List<LinkArgument> getLinkArguments() {
        def arguments = new ArrayList<LinkArgument>()
        arguments.add(createLinkArgument("java.,org.xml.,javax.,org.xml.", "http://docs.oracle.com/javase/8/docs/api/"))
        arguments.add(createLinkArgument("groovy.,org.codehaus.groovy.", "http://docs.groovy-lang.org/latest/html/api/"))

        return arguments
    }

    private LinkArgument createLinkArgument(String packageName, String href) {
        def argument = new LinkArgument()
        argument.setPackages(packageName)
        argument.setHref(href)
        return argument
    }
}
