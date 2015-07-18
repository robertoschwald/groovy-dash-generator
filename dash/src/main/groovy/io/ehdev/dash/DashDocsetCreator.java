package io.ehdev.dash;

import io.ehdev.dash.parser.ClassDocParser;
import io.ehdev.dash.render.IndexPage;
import io.ehdev.dash.render.Renderer;
import io.ehdev.dash.sql.DashSql;

import java.io.File;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.List;

public class DashDocsetCreator {

    final private List<File> docRoots;
    final private File destination;
    final private File indexDir;

    public DashDocsetCreator(List<File> docRoots, File destination, File indexDir) {
        this.docRoots = docRoots;
        this.destination = destination;
        this.indexDir = indexDir;
    }

    public DashDocsetCreator(List<File> docRoots, File destination) {
        this(docRoots, destination, null);
    }

    public void createDashDocset(InputStream image, String name) throws SQLException, ClassNotFoundException {
        destination.mkdirs();
        IconWriter.writeIcon(image, destination);

        new PListCreator().createPlist(destination, findIndexPage(), name);
        DashSql dashSql = new DashSql(destination);
        new Renderer(docRoots, destination).renderDocs();
        new ClassDocParser(docRoots, dashSql).parseDocs();
        IndexPage.copyIndexs(destination, indexDir);
    }

    private String findIndexPage() {
        if(null == indexDir) {
            return "index.html";
        } else {
            return indexDir.getName() + "/index.html";
        }
    }
}
