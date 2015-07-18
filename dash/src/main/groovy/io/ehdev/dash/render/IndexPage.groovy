package io.ehdev.dash.render

import org.apache.commons.io.FileUtils

class IndexPage {

    static public void copyIndexs(File dest, File indexFolder) {
        if(null == indexFolder) {
            return;
        }

        File indexDest = new File(dest, 'Contents/Resources/Documents/' + indexFolder.getName())
        indexDest.mkdirs()
        FileUtils.copyDirectory(indexFolder, indexDest)

        indexDest.eachFileRecurse {
            it.text = it.text.replaceAll('\\.\\./javadoc', '../groovydoc')
        }
    }
}
