package nl.rug.search.odr.export;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
public class TeXExportException extends RuntimeException {

    public TeXExportException() {
    }

    public TeXExportException(String message) {
        super(message);
    }

    public TeXExportException(Throwable cause) {
        super(cause);
    }

    public TeXExportException(String message, Throwable cause) {
        super(message, cause);
    }
}
