
package nl.rug.search.odr;

import com.thoughtworks.selenium.SeleneseTestCase;
import com.thoughtworks.selenium.Wait;
import org.junit.Ignore;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
@Ignore
public abstract class AbstractSelenseTestCase extends SeleneseTestCase implements Settings {

    

    @Override
    public void setUp() throws Exception {
        setUp(URL, BROWSER);
        clearDatabase();
    }

    public void clearDatabase() {
        selenium.open("/web-interface/fillDb.html");
        selenium.click("fillDatabaseForm:clearDatabase");
    }

    public void sleep(final long millis) {
        new Wait("Error in Sleep") {

            @Override
            public boolean until() {
                try {
                    Thread.sleep(millis);
                } catch (Exception e) {
                    return false;
                }
                return true;
            }
        };
    }

    public void waitForAjaxRequest() {
        sleep(AJAX_SLEEP_MILLIS);
    }
}
