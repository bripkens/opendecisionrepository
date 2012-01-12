package nl.rug.search.odr.ws.connection;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
abstract class AbstractWebServiceFacade {

    private static final String APPLICATION_CONTEXT = "/web-service/rest/";
    
    private final String baseURL;

    private final WebServiceConnection connection;

    AbstractWebServiceFacade(String baseURL, WebServiceConnection connection) {
        this.baseURL = baseURL + APPLICATION_CONTEXT;
        this.connection = connection;
    }

    WebServiceConnection getConnection() {
        return connection;
    }

    String buildUrl(String extension) {
        return baseURL.concat(extension);
    }

    String buildUrl(String format, Object... args) {
        return baseURL.concat(String.format(format, args));
    }
}
