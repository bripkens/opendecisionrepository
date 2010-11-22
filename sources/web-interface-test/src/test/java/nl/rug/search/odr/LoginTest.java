package nl.rug.search.odr;

import com.thoughtworks.selenium.Selenium;
import org.junit.Ignore;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */

public class LoginTest extends AbstractSelenseTestCase {

    public static void loginUserWithDefaulCredentials(Selenium selenium) {
        loginUser(selenium, RegistrationTest.EMAIL, RegistrationTest.PASSWORD);
    }




    public static void logout(Selenium selenium) {
        selenium.click("logoutForm:logoutButton");
        sleep(1000);
    }




    public static void loginUser(Selenium selenium, String email, String password) {
        selenium.type("loginForm:inLoginEmail", email);
        selenium.type("loginForm:inLoginPassword", password);
        selenium.click("loginForm:loginButton");
        waitForPageToLoad(selenium);
    }
    public void testLoginErrorVisibilityNoInput() throws Exception {
        assertFalse(selenium.isVisible("loginForm:loginErrorMessage"));
        selenium.click("loginForm:loginButton");

        waitForAjaxRequest();

        assertTrue(selenium.isVisible("loginForm:loginErrorMessage"));
    }




    public void testLoginErrorVisibilitySomeInput() throws Exception {
        assertFalse(selenium.isVisible("loginForm:loginErrorMessage"));
        selenium.type("loginForm:inLoginEmail", "someweirdaddress");
        selenium.type("loginForm:inLoginPassword", "dsadsa");
        selenium.click("loginForm:loginButton");

        waitForAjaxRequest();

        assertTrue(selenium.isVisible("loginForm:loginErrorMessage"));
    }




    public void testValidLogin() throws Exception {
        RegistrationTest.registerUserWithDefaulCredentials(selenium);

        waitForAjaxRequest();

        open();

        loginUserWithDefaulCredentials(selenium);

        waitForPageToLoad();

        verifyTrue(selenium.isElementPresent("logoutForm:logoutButton"));
    }




    public void testLoginAndLogout() {
        RegistrationTest.registerUserWithDefaulCredentials(selenium);

        waitForAjaxRequest();

        open();

        loginUserWithDefaulCredentials(selenium);

        waitForPageToLoad();
        selenium.click("logoutForm:logoutButton");
        waitForPageToLoad();

        verifyTrue(selenium.isTextPresent("Register"));
    }
}
