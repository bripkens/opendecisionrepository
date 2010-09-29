package nl.rug.search.org;

import com.thoughtworks.selenium.SeleneseTestCase;
import com.thoughtworks.selenium.Wait;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
public class LoginTest extends SeleneseTestCase {

    @Override
    public void setUp() throws Exception {
        setUp("http://localhost:8080/web-interface/");
    }

    public void testLoginErrorVisibility() throws Exception {
        selenium.open("/web-interface/");
        assertEquals("display: none;", selenium.getAttribute("loginForm:j_idt22@style"));
        selenium.type("loginForm:inLoginEmail", "someweirdaddress");
        selenium.type("loginForm:inLoginPassword", "dsadsa");
        selenium.click("loginForm:j_idt25");

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

        assertNotEquals("display: none;", selenium.getAttribute("loginForm:j_idt22@style"));
    }

    public void testValidLogin() throws Exception {
        // test requires a user with name: Ben Ripkens
        // email: ben@ben.de
        // password: 12345

        selenium.open("/web-interface/index.html");
        selenium.type("loginForm:inLoginEmail", "ben@ben.de");
        selenium.type("loginForm:inLoginPassword", "12345");
        selenium.click("loginForm:j_idt25");
        selenium.waitForPageToLoad("30000");
        verifyTrue(selenium.isTextPresent("Hello Ben Ripkens"));
        selenium.click("logoutForm:j_idt22");
        selenium.waitForPageToLoad("30000");
        verifyTrue(selenium.isTextPresent("Register"));
    }
}
