
package nl.rug.search.odr;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
public abstract class DropAllTables implements DatabaseSettings {
    public static void main(String[] args) {
        new DatabaseCleaner().dropAllTables();
    }
}
