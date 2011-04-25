package nl.rug.search.odr;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
public class DatabaseCleaner {

    // <editor-fold defaultstate="collapsed" desc="don't changed anything below this comment">
    private static final Logger logger = Logger.getLogger(DatabaseCleaner.class.getName());




    static {
        try {
            Class.forName(DatabaseSettings.DRIVER_CLASS).
                    newInstance();

        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }




    private final String connectionString;

    private final int iterations;

    private final String[] skipTables;

    private Connection con;

    private List<String> tableNames = new ArrayList<String>();




    public DatabaseCleaner() {
        this(DatabaseSettings.CONNECTION_STRING, DatabaseSettings.ITERATIONS, DatabaseSettings.SKIP_TABLES);
    }




    public DatabaseCleaner(String connectionString, int iterations, String[] skipTables) {
        this.connectionString = connectionString;
        this.iterations = iterations;
        this.skipTables = skipTables;
    }




    public DatabaseCleaner clear() {
        try {
            establishConnection();

            analyseDatabase();

            boolean entriesLeft = clearDatabase();

            if (entriesLeft) {
                logger.log(Level.WARNING, "The specified amount of {0} iterations was not enough to clear "
                        + "the whole database.", iterations);
            }
        } finally {
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException ex) {
                }
            }
        }

        return this;
    }




    private void establishConnection() {
        try {
            con = DriverManager.getConnection(connectionString);
        } catch (SQLException ex) {
            throw new RuntimeException("An exception occured while trying to connect to the database.", ex);
        }


    }




    private void analyseDatabase() {
        try {
            DatabaseMetaData metaData = con.getMetaData();

            ResultSet result = metaData.getTables(null, null, "%", new String[]{"TABLE"});

            while (result.next()) {
                String tableName = result.getString("TABLE_NAME");

                if (!shouldBeSkipped(tableName)) {
                    tableNames.add(tableName);
                }
            }
        } catch (SQLException ex) {
            throw new RuntimeException("An exception occured while trying to analyse the database.", ex);
        }
    }




    private boolean shouldBeSkipped(String name) {
        for (String skipTableName : skipTables) {
            if (skipTableName.equalsIgnoreCase(name)) {
                return true;
            }
        }

        return false;
    }




    private boolean clearDatabase() {
        boolean entriesLeft = true;

        for (int i = 0; i < iterations && entriesLeft; i++) {
            entriesLeft = clearTables();

            if (!entriesLeft) {
                logger.log(Level.INFO, "No database entries left after {0} iteration(s).", i + 1);
            }
        }

        return entriesLeft;
    }




    private boolean clearTables() {
        boolean entriesLeft = false;

        for (String tableName : tableNames) {
            entriesLeft = clearSingleTable(tableName) || entriesLeft;
        }

        return entriesLeft;
    }




    private boolean clearSingleTable(String tableName) {
        try {
            boolean entriesLeft = false;
            
            ResultSet result = con.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE).
                    executeQuery("SELECT * FROM ".concat(tableName));

            while (!result.isClosed() && result.next()) {
                entriesLeft = deleteRow(result) || entriesLeft;
            }


            return entriesLeft;
        } catch (SQLException ex) {
            throw new RuntimeException("Can't read table contents from table ".
                    concat(tableName), ex);
        }
    }




    private boolean deleteRow(ResultSet result) {
        try {
            result.deleteRow();

            return false;
        } catch (SQLException ex) {
            return true;
        }
    }




    public DatabaseCleaner dropAllTables() {
        clear();

        try {
            establishConnection();

            analyseDatabase();

            dropTables();
        } finally {
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException ex) {
                }
            }
        }

        return this;
    }




    private void dropTables() {

        for (int i = 0; i < iterations && !tableNames.isEmpty(); i++) {
            Iterator<String> tableNamesIt = tableNames.iterator();

            while (tableNamesIt.hasNext()) {
                try {
                    con.createStatement().executeUpdate("DROP TABLE ".concat(tableNamesIt.next()));
                    tableNamesIt.remove();
                } catch (SQLException ex) {
                }
            }

        }
    }




    public static void bruteForceCleanup() {
        new DatabaseCleaner().clear();
    }




    public static void main(String[] args) {
        new DatabaseCleaner().clear();
    }

    // </editor-fold>



}



