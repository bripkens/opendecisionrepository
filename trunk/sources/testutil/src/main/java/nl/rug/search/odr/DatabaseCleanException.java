package nl.rug.search.odr;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
public class DatabaseCleanException extends RuntimeException {

    public DatabaseCleanException() {
    }

    public DatabaseCleanException(String message) {
        super(message);
    }

    public DatabaseCleanException(Throwable cause) {
        super(cause);
    }

    public DatabaseCleanException(String message, Throwable cause) {
        super(message, cause);
    }
}
