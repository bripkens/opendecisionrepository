package nl.rug.search.odr.ws.connection;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
public class WebServiceSetupException extends WebServiceException {

    public WebServiceSetupException() {
    }

    public WebServiceSetupException(String message) {
        super(message);
    }

    public WebServiceSetupException(Throwable cause) {
        super(cause);
    }

    public WebServiceSetupException(String message, Throwable cause) {
        super(message, cause);
    }
}
