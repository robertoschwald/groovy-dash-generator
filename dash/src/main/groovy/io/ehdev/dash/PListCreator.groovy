package io.ehdev.dash

import groovy.xml.MarkupBuilder

class PListCreator {

    public void createPlist(File rootDir, String indexLocation, String name) {
        def plist = new File(rootDir, 'Contents/Info.plist')
        plist.parentFile.mkdirs()
        def builder = new MarkupBuilder(plist.newWriter())
        builder.plist(version: '1.0') {
            dict {
                key('CFBundleIdentifier')
                string(name)

                key('CFBundleName')
                string(name.capitalize())

                key('DocSetPlatformFamily')
                string(name)

                key('isDashDocset')
                'true'()

                key('dashIndexFilePath')
                string(indexLocation)

                key('DashDocSetFamily')
                string('dashtoc')
            }
        }

    }
}
