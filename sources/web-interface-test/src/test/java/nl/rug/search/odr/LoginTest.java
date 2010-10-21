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
        selenium.waitForPageToLoad("30000");
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

        selenium.open("/web-interface/index.html");

        loginUserWithDefaulCredentials(selenium);
        
        selenium.waitForPageToLoad("30000");
        
        verifyTrue(selenium.isElementPresent("logoutForm:logoutButton"));
    }




    public void testLoginAndLogout() {
        RegistrationTest.registerUserWithDefaulCredentials(selenium);

        waitForAjaxRequest();

        selenium.open("/web-interface/index.html");

        loginUserWithDefaulCredentials(selenium);

        selenium.waitForPageToLoad("30000");
        selenium.click("logoutForm:logoutButton");
        selenium.waitForPageToLoad("30000");
        
        verifyTrue(selenium.isTextPresent("Register"));
    }
}
