package nl.rug.search.odr;

import com.thoughtworks.selenium.SeleneseTestCase;
import com.thoughtworks.selenium.Wait;
import org.junit.Ignore;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
@Ignore
public class LoginTest extends SeleneseTestCase implements Settings {

    @Override
    public void setUp() throws Exception {
        setUp(URL, BROWSER);
    }

    public void testLoginErrorVisibilityNoInput() throws Exception {
        selenium.open("/web-interface/");
        assertFalse(selenium.isVisible("loginForm:loginErrorMessage"));
        selenium.click("loginForm:loginButton");

        new Wait("Error in wait") {

            @Override
            public boolean until() {
                try {
                    Thread.sleep(100);
                } catch (Exception e) {
                    return false;
                }
                return true;
            }
        };

        assertTrue(selenium.isVisible("loginForm:loginErrorMessage"));
    }

    public void testLoginErrorVisibilitySomeInput() throws Exception {
        selenium.open("/web-interface/");
        assertFalse(selenium.isVisible("loginForm:loginErrorMessage"));
        selenium.type("loginForm:inLoginEmail", "someweirdaddress");
        selenium.type("loginForm:inLoginPassword", "dsadsa");
        selenium.click("loginForm:loginButton");

        new Wait("Error in wait") {

            @Override
            public boolean until() {
                try {
                    Thread.sleep(100);
                } catch (Exception e) {
                    return false;
                }
                return true;
            }
        };

        assertTrue(selenium.isVisible("loginForm:loginErrorMessage"));
    }

    public void testValidLogin() throws Exception {
        // test requires a user with name: Ben Ripkens
        // email: ben@ben.de
        // password: 12345

        selenium.open("/web-interface/index.html");
        selenium.type("loginForm:inLoginEmail", "ben@ben.de");
        selenium.type("loginForm:inLoginPassword", "12345");
        selenium.click("loginForm:loginButton");
        selenium.waitForPageToLoad("30000");
        verifyTrue(selenium.isElementPresent("logoutForm:logoutButton"));
    }

    public void testLoginAndLogout() {
        // test requires a user with name: Ben Ripkens
        // email: ben@ben.de
        // password: 12345

        selenium.open("/web-interface/index.html");
        selenium.type("loginForm:inLoginEmail", "ben@ben.de");
        selenium.type("loginForm:inLoginPassword", "12345");
        selenium.click("loginForm:loginButton");
        selenium.waitForPageToLoad("30000");
        selenium.click("logoutForm:logoutButton");
        selenium.waitForPageToLoad("30000");
        verifyTrue(selenium.isTextPresent("Register"));
    }
}
