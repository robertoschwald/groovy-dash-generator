package io.ehdev.dash;

import io.ehdev.dash.sql.DashSql;

import java.io.File;
import java.sql.SQLException;
import java.util.List;

public class DashDocsetCreator {

    final private List<File> docRoots;
    final private File destination;

    public DashDocsetCreator(List<File> docRoots, File destination) {
        this.docRoots = docRoots;
        this.destination = destination;
    }

    public void createDashDocset() throws SQLException, ClassNotFoundException {
        destination.mkdirs();
        IconWriter.writeIcon(destination);
        new PListCreator().createPlist(destination);
        DashSql dashSql = new DashSql(destination);
        GroovyDocParser groovyDocParser = new GroovyDocParser(dashSql, destination);

        for (File docRoot : docRoots) {
            groovyDocParser.processDocRoot(docRoot);
        }

        new DashTableCreator(dashSql, destination).addTableEntries();
    }
}
