package nl.rug.search.odr;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
public interface DatabaseSettings {

    int ITERATIONS = 5;

    String[] SKIP_TABLES = new String[]{"SEQUENCE"};
}