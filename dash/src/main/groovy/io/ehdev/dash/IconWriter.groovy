package io.ehdev.dash

class IconWriter {

    static public void writeIcon(InputStream image, File destination) {
        if(null != image) {
            new File(destination, 'icon.png').setBytes(image.getBytes())
        }
    }
}
