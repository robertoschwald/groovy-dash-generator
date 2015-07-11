package io.ehdev.dash;

public enum ObjectType {
    CLASS("Class"),
    ENUM("Enum"),
    INTERFACE("Interface"),
    CONSTRUCTOR("Constructor"),
    FIELD("Field"),
    ANNOTATION("Annotation"),
    METHOD("Method");

    private final String value;

    ObjectType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
