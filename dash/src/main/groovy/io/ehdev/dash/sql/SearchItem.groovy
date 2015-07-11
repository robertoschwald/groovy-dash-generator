package io.ehdev.dash.sql

public class SearchItem {
    private final String name;
    private final String type;
    private final String file;
    private final String anchor;

    public SearchItem(String name, String type, String path) {
        this.name = name;
        this.type = type;
        String[] split = path.split("#");
        this.file = split[0]
        anchor = split.drop(1).join('#')
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    String getFile() {
        return file
    }

    String getAnchor() {
        return anchor
    }


    @Override
    public String toString() {
        return "SearchItem{" +
                "name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", file='" + file + '\'' +
                ", anchor='" + anchor + '\'' +
                '}';
    }
}
