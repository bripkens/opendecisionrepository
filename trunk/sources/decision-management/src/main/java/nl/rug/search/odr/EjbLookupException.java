package nl.rug.search.odr;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
public class EjbLookupException extends RuntimeException {

    public EjbLookupException() {
    }

    public EjbLookupException(String message) {
        super(message);
    }

    public EjbLookupException(Throwable cause) {
        super(cause);
    }

    public EjbLookupException(String message, Throwable cause) {
        super(message, cause);
    }
}
