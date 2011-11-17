package nl.rug.search.odr.ws.connection;

/**
 *
 * @author Stefan
 */
public class WebServiceConnectionException extends RuntimeException {

    private final int responseCode;
    private Throwable cause;

    public WebServiceConnectionException(Throwable cause, int responseCode) {
        this.cause = cause;
        this.responseCode = responseCode;
    }

    public int getResponseCode() {
        return responseCode;
    }

    @Override
    public Throwable getCause() {
        return this.cause;
    }

    public boolean isUnauthorizedStatus() {
        return responseCode == 401;
    }

    public boolean isNotFound() {
        return responseCode == 404;
    }
}
