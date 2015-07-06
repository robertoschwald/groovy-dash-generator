package io.ehdev.dash;

import org.codehaus.groovy.ant.Groovydoc;

public class ModifiedGroovyDoc extends Groovydoc {

    protected String[] getClassTemplates() {
        return new String[]{
            "dash-docs/classDocName.html"
        };
    }

}
