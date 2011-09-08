package nl.rug.search.odr;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
public class DatabaseSettings {

    public static String CONNECTION_STRING = "jdbc:derby://localhost:1527/sun-appserv-samples";

    public static String DRIVER_CLASS = "org.apache.derby.jdbc.ClientDriver";

    public static int ITERATIONS = 5;

    public static String[] SKIP_TABLES = new String[]{"SEQUENCE"};
}