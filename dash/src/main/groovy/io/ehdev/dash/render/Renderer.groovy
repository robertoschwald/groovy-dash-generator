package io.ehdev.dash.render

import org.codehaus.groovy.tools.groovydoc.*

class Renderer {
    final private List<File> sourceSets;
    final private File htmlLocation

    Renderer(List<File> sourceSets, File dest) {
        this.sourceSets = sourceSets
        this.htmlLocation = new File(dest, 'Contents/Resources/Documents/groovydoc')
    }

    public void renderDocs() {
        def paths = sourceSets.collect { it.getAbsolutePath() }

        def properties = new Properties()
        properties.setProperty('packagenames', '**.*')
        properties.setProperty('use', 'true')

        def tool = new GroovyDocTool(new ClasspathResourceManager(), paths as String[], [] as String[], [] as String[], ['rootdir/classDocName.html'] as String[], getLinkArguments(), properties)

        def files = FindFilesToRender.findFilesToProcess(sourceSets)

        tool.add(files)
        OutputTool outputTool = new FileOutputTool()
        tool.renderToOutput(outputTool, htmlLocation.getAbsolutePath())
        new File(htmlLocation, 'stylesheet.css').text = getClass().getResource('/rootdir/stylesheet.css').text
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
