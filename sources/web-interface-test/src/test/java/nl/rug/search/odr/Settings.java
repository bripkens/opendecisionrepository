package nl.rug.search.odr;

import org.junit.Ignore;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
@Ignore
public interface Settings {

    String URL = "http://localhost:8080/web-interface/";

    String CONTEXT_PATH = "/web-interface/";

    String BROWSER = "*firefox";

    long AJAX_SLEEP_MILLIS = 400;

    String TIMEOUT_MILLIS = "30000";

}
