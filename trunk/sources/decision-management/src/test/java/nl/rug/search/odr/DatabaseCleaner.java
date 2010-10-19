package nl.rug.search.odr;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
public abstract class DatabaseCleaner {

    public static final String CONNECTION_STRING = "jdbc:derby://localhost:1527/sun-appserv-samples";

    public static final int ITERATIONS = 3;

    public static final String[][] TABLES = new String[][]{
        {"COMPONENTVALUE", "ID"},
        {"DECISION", "ID"},
        {"DECISION_COMPONENTVALUE", "DECISION_ID"},
        {"DECISION_VERSION", "DECISION_ID"},
        {"DECISIONTEMPLATE", "ID"},
        {"DECISIONTEMPLATE_TEMPLATECOMPONENT", "DECISIONTEMPLATE_ID"},
        {"ITERATION", "ID"},
        {"OPRLINK", "ID"},
        {"PERSON", "ID"},
        {"PROJECT", "ID"},
        {"PROJECT_DECISION", "PROJECT_ID"},
        {"PROJECT_ITERATION", "PROJECT_ID"},
        {"PROJECT_STAKEHOLDERROLE", "PROJECT_ID"},
        {"PROJECTMEMBER", "ID"},
        {"STAKEHOLDERROLE", "ID"},
        {"TEMPLATECOMPONENT", "ID"},
        {"VERSION", "ID"}};




    static {
        try {
            Class.forName("org.apache.derby.jdbc.ClientDriver").
                    newInstance();

        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }




    public static void bruteForceCleanup() {
        Connection con;

        try {
            con = DriverManager.getConnection(CONNECTION_STRING);
        } catch (SQLException ex) {
            throw new RuntimeException("Can't establish a connection to the database", ex);
        }


        for (int i = 0; i < ITERATIONS; i++) {
            clearDatabase(con);
        }


        try {
            con.close();
        } catch (SQLException ex) {
        }
    }




    private static void clearDatabase(Connection con) {
        for (int i = 0; i < TABLES.length; i++) {
            clearTable(con, i);
        }
    }




    private static void clearTable(Connection con, int tableId) {
        try {
            ResultSet result = con.createStatement().
                    executeQuery("SELECT ".concat(TABLES[tableId][1]).
                    concat(" FROM ").
                    concat(TABLES[tableId][0]));

            while (result.next()) {
                deleteRow(con, tableId, result.getLong(1));
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Can't read table contents. Trying to read column " + TABLES[tableId][1] +
                    " from table " + TABLES[tableId][0], ex);
        }

    }




    private static void deleteRow(Connection con, int tableId, long rowId) {

        try {
            con.createStatement().
                    executeUpdate("DELETE FROM ".concat(TABLES[tableId][0]).
                    concat(" WHERE ").
                    concat(TABLES[tableId][1]).
                    concat(" = ").
                    concat(String.valueOf(rowId)));

        } catch (SQLException ex) {
            // may be thrown because of constraints, don't do anything
        }
    }




    public static void main(String[] args) {
        DatabaseCleaner.bruteForceCleanup();
    }
}
