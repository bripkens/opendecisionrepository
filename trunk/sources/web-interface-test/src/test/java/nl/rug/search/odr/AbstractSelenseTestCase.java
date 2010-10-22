package nl.rug.search.odr;

import com.thoughtworks.selenium.SeleneseTestCase;
import com.thoughtworks.selenium.Selenium;
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
        super.setUp(URL, BROWSER);
        clearDatabase();
        open();
    }




    public void clearDatabase() {
        open("fillDb.html");
        selenium.click("fillDatabaseForm:clearDatabase");
    }




    public void addStakeholderRoles() {
        open("fillDb.html");
        selenium.click("fillDatabaseForm:addRoles");
        waitForAjaxRequest();
    }




    public void addStates() {
        open("fillDb.html");
        selenium.click("fillDatabaseForm:addStates");
        waitForAjaxRequest();
    }




    public void addTemplates() {
        open("fillDb.html");
        selenium.click("fillDatabaseForm:addTemplates");
        waitForAjaxRequest();
    }




    /**
     * 
     * @param path should not start with a slash
     */
    public void open(String path) {
        open(selenium, path);
    }




    public void open() {
        open("");
    }




    public static void open(Selenium selenium) {
        open(selenium, "");
    }




    /**
     *
     * @param path should not start with a slash
     */
    public static void open(Selenium selenium, String path) {
        selenium.open(CONTEXT_PATH.concat(path));
    }




    public static void sleep(final long millis) {
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




    public static void waitForAjaxRequest() {
        sleep(AJAX_SLEEP_MILLIS);
    }
}
