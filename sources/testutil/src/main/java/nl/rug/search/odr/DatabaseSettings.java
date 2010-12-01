package nl.rug.search.odr;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
public interface DatabaseSettings {

    String CONNECTION_STRING = "jdbc:derby://127.0.0.1:1527/sun-appserv-samples";

    String DRIVER_CLASS = "org.apache.derby.jdbc.ClientDriver";

    int ITERATIONS = 5;

    String[] SKIP_TABLES = new String[]{"SEQUENCE"};
}



