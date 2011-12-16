package nl.rug.search.odr.ws.connection;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
public class WebServiceException extends RuntimeException {

    public WebServiceException() {
    }

    public WebServiceException(String message) {
        super(message);
    }

    public WebServiceException(Throwable cause) {
        super(cause);
    }

    public WebServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
