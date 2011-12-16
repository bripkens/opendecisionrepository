package nl.rug.search.odr.ws.connection;

import org.apache.http.StatusLine;

/**
 * This exception will be raised when using either the
 * {@link WebServiceConnection} or {@link WebServiceFacade} in cases where the
 * requests results in an HTTP status code < 200 or > 299.
 * 
 * @author Stefan
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
public class WebServiceRequestException extends WebServiceException {

    private static final int HTTP_FORBIDDEN_STATUS = 401;
    private static final int HTTP_NOT_FOUND_STATUS = 404;
    
    private final StatusLine statusLine;

    WebServiceRequestException(StatusLine statusLine, String error) {
        super(statusLine.getStatusCode() + ": " + statusLine.getReasonPhrase()
                + "; " + error);
        this.statusLine = statusLine;
    }

    /**
     * Get the response code which was sent by the server.
     * 
     * @return The HTTP response code
     */
    public int getResponseCode() {
        return statusLine.getStatusCode();
    }

    /**
     * Shortcut for checking for 401 HTTP error codes.
     * 
     * @return True when the response code is 401.
     */
    public boolean isUnauthorizedStatus() {
        return getResponseCode() == HTTP_FORBIDDEN_STATUS;
    }

    /**
     * Shortcut for checking for 404 HTTP error codes.
     * 
     * @return True when the response code is 404.
     */
    public boolean isNotFoundStatus() {
        return getResponseCode() == HTTP_NOT_FOUND_STATUS;
    }
}
