package nl.rug.search.odr;

import com.thoughtworks.selenium.Selenium;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
public class LoginTest extends AbstractSelenseTestCase {

    public static void loginUserWithDefaulCredentials(Selenium selenium) {
        selenium.type("loginForm:inLoginEmail", RegistrationTest.EMAIL);
        selenium.type("loginForm:inLoginPassword", RegistrationTest.PASSWORD);
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
