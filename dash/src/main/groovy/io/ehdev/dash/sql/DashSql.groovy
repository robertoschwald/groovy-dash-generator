package io.ehdev.dash.sql

import groovy.sql.Sql
import io.ehdev.dash.ObjectType

import java.sql.SQLException

public class DashSql {

    private final Sql instance;
    private static final String insertStatement = "INSERT OR IGNORE INTO searchIndex(name, type, path) VALUES ('%s', '%s', '%s');";

    public DashSql(File destDir) throws SQLException, ClassNotFoundException {
        File sqlFile = new File(destDir, "Contents/Resources/docSet.dsidx");
        sqlFile.getParentFile().mkdirs();
        instance = Sql.newInstance("jdbc:sqlite:" + sqlFile.getAbsolutePath(), "org.sqlite.JDBC");

        def table_exists = instance.firstRow('SELECT name FROM sqlite_master WHERE type=\'table\' AND name=\'searchIndex\';')
        if(null == table_exists || table_exists.size() == 0) {
            instance.execute("CREATE TABLE searchIndex(id INTEGER PRIMARY KEY, name TEXT, type TEXT, path TEXT);");
            instance.execute("CREATE UNIQUE INDEX anchor ON searchIndex (name, type, path);");
        }
    }

    public void insert(String name, ObjectType type, String path) throws SQLException {
        instance.execute(String.format(insertStatement, name, type.getValue(), path));
    }

    public List<SearchItem> getSearchItems() {
        List<SearchItem> searchItems = new ArrayList<SearchItem>()
        instance.eachRow('select * from searchIndex') { row ->
            searchItems.add(new SearchItem(row.name, row.type, row.path))
        }
        return searchItems
    }

}
