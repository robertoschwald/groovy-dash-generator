package io.ehdev.dash

class IconWriter {

    static public void writeIcon(File destination) {
        new File(destination, 'icon.png').setBytes(getClass().getResourceAsStream('/gradle_logo.png').getBytes())
    }
}
