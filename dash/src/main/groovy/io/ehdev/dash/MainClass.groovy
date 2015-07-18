package io.ehdev.dash

import groovy.util.logging.Slf4j

@Slf4j
class MainClass {

    public static void main(String[] args) {
        OptionAccessor optionAccessor = creatOptionAccessor(args)

        if(!optionAccessor) {
            return;
        }

        def name = optionAccessor.getProperty('name') as String
        def sourceRoots = getSourceRoots(optionAccessor)
        def outputDir = new File(optionAccessor.getProperty('output-dir') as String, "${name}.docset")
        outputDir.mkdirs()

        log.info("Writing output to {}", outputDir.getAbsolutePath())

        File indexDir = null;
        if(optionAccessor.getProperty('index')) {
            indexDir = new File(optionAccessor.getProperty('index') as String)
            log.info('Using {} as the index of the docset', indexDir.absolutePath)
        }

        InputStream image = null;
        if(optionAccessor.getProperty('image')) {
            def file = new File(optionAccessor.getProperty('image') as String)
            if(file.exists()) {
                log.info("Using {} as image", file.getAbsolutePath())
                image = file.newInputStream()
            } else {
                log.warn("Unable to use image {}, as it doesn't exist", file.getAbsolutePath())
            }
        }

        new DashDocsetCreator(sourceRoots, outputDir, indexDir).createDashDocset(image, name)
    }

    private static List<File> getSourceRoots(OptionAccessor optionAccessor) {
        List<File> files = new ArrayList<File>()
        optionAccessor.arguments().each {
            def file = new File(it)
            if (file.exists()) {
                log.info('Using {} as a source root', file.absolutePath)
                files.add(file)
            }
        }

        return files;
    }

    private static OptionAccessor creatOptionAccessor(String[] args) {
        def cliBuilder = new CliBuilder(usage: 'generate-docs [options] path [path ..]')
        cliBuilder._(longOpt: 'image', args: 1, argName: 'IMAGE', 'File that should be used as the index (32x32 png)')
        cliBuilder._(longOpt: 'index', args: 1, argName: 'PATH', 'Link to the folder containing the index file')
        cliBuilder._(longOpt: 'output-dir', args: 1, argName: "OUTPUT", required: true, 'Directory to put the output to')
        cliBuilder._(longOpt: 'name', args: 1, argName: "name", required: true, 'The name of the docset')
        cliBuilder.h(longOpt: 'help', 'Display this message')


        def optionAccessor = cliBuilder.parse(args)


        if(optionAccessor.getProperty('help') as Boolean) {
            cliBuilder.usage();
            return null;
        }

        return optionAccessor
    }
}
