package io.ehdev.dash.render
import groovy.io.FileType
import groovy.util.logging.Slf4j
import org.apache.commons.io.FilenameUtils

@Slf4j
class FindFilesToRender {

    static List<String> findFilesToProcess(List<File> sourceSets) {
        def files = []
        for(File file: sourceSets) {
            def path = file.absolutePath
            if(file.isDirectory() && !path.endsWith('/')) {
                path += '/'
            }
            file.eachFileRecurse(FileType.FILES) {
                def extension = FilenameUtils.getExtension(it.name)
                if('java'.equalsIgnoreCase(extension) || 'groovy'.equalsIgnoreCase(extension)) {
                    files << it.absolutePath - path
                }
            }
        }
        return files;
    }
}
