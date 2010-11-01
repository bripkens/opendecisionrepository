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

    public static void logout(Selenium selenium){
        selenium.click("logoutForm:logoutButton");
        selenium.waitForPageToLoad(TIMEOUT_MILLIS);
    }


    public static void loginUser(Selenium selenium, String email, String password) {
        selenium.type("loginForm:inLoginEmail", email);
        selenium.type("loginForm:inLoginPassword", password);
        selenium.click("loginForm:loginButton");
        selenium.waitForPageToLoad(TIMEOUT_MILLIS);
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

        selenium.waitForPageToLoad(TIMEOUT_MILLIS);

        verifyTrue(selenium.isElementPresent("logoutForm:logoutButton"));
    }




    public void testLoginAndLogout() {
        RegistrationTest.registerUserWithDefaulCredentials(selenium);

        waitForAjaxRequest();

        open();

        loginUserWithDefaulCredentials(selenium);

        selenium.waitForPageToLoad(TIMEOUT_MILLIS);
        selenium.click("logoutForm:logoutButton");
        selenium.waitForPageToLoad(TIMEOUT_MILLIS);

        verifyTrue(selenium.isTextPresent("Register"));
    }
}
