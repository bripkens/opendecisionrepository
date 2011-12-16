package nl.rug.search.odr.ws.connection;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
public class WebServiceMarshalException extends WebServiceException {

    public WebServiceMarshalException() {
    }

    public WebServiceMarshalException(String message) {
        super(message);
    }

    public WebServiceMarshalException(Throwable cause) {
        super(cause);
    }

    public WebServiceMarshalException(String message, Throwable cause) {
        super(message, cause);
    }
}
