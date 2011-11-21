package nl.rug.search.odr.ws.connection;

import org.apache.http.StatusLine;

/**
 *
 * @author Stefan
 */
public class WebServiceConnectionException extends RuntimeException {

    private final StatusLine statusLine;

    WebServiceConnectionException(StatusLine statusLine) {
        this.statusLine = statusLine;
    }

    public int getResponseCode() {
        return statusLine.getStatusCode();
    }

    public boolean isUnauthorizedStatus() {
        return getResponseCode() == 401;
    }

    public boolean isNotFound() {
        return getResponseCode() == 404;
    }
    
    public String getReason() {
        return statusLine.getReasonPhrase();
    }
}
