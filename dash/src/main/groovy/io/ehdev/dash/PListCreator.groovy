package io.ehdev.dash

import groovy.xml.MarkupBuilder

class PListCreator {

    public void createPlist(File rootDir) {
        def plist = new File(rootDir, 'Contents/Info.plist')
        plist.parentFile.mkdirs()
        def builder = new MarkupBuilder(plist.newWriter())
        builder.plist(version: '1.0') {
            dict {
                key('CFBundleIdentifier')
                string('gradle')

                key('CFBundleName')
                string('Gradle')

                key('DocSetPlatformFamily')
                string('gradle')

                key('isDashDocset')
                'true'()

                key('DashDocSetFamily')
                string('dashtoc')
            }
        }

    }
}
