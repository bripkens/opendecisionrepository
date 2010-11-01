package nl.rug.search.odr;

import com.thoughtworks.selenium.Selenium;
import org.junit.Ignore;
import org.junit.Test;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
@Ignore
public class RegistrationTest extends AbstractSelenseTestCase {

    public static final String USER_NAME = "Ben Ripkens";

    public static final String PASSWORD = "12345";

    public static final String EMAIL = "ben@ripkens.de";




    public static void registerUserWithDefaulCredentials(Selenium selenium) {
        selenium.click("loginForm:registerLink");
        selenium.waitForPageToLoad(TIMEOUT_MILLIS);
        selenium.type("registerForm:inPassword", PASSWORD);
        selenium.type("registerForm:inName", USER_NAME);
        selenium.type("registerForm:inEmail", EMAIL);
        selenium.click("registerForm:registrationSubmitButton");
    }




    @Test
    public void testRegister() {
        registerUserWithDefaulCredentials(selenium);

        waitForAjaxRequest();

        verifyTrue(selenium.isTextPresent("successful"));
    }




    @Test
    public void testValidation() {
        registerUserWithDefaulCredentials(selenium);

        waitForAjaxRequest();

        open();
        selenium.click("loginForm:registerLink");
        selenium.waitForPageToLoad(TIMEOUT_MILLIS);
        selenium.type("registerForm:inName", USER_NAME);
        selenium.fireEvent("registerForm:inName", "blur");
        waitForAjaxRequest();
        verifyTrue(selenium.isTextPresent("User name is not unique"));
        selenium.type("registerForm:inPassword", "1234");
        selenium.fireEvent("registerForm:inPassword", "blur");
        waitForAjaxRequest();
        verifyTrue(selenium.isTextPresent("Has less than allowed 5 chars"));
        selenium.type("registerForm:inEmail", "wrongFormat");
        selenium.fireEvent("registerForm:inEmail", "blur");
        waitForAjaxRequest();
        verifyTrue(selenium.isTextPresent("Please provide a valid e-mail address"));
        selenium.type("registerForm:inEmail", EMAIL);
        selenium.fireEvent("registerForm:inEmail", "blur");
        waitForAjaxRequest();
        verifyTrue(selenium.isTextPresent("This email is already registered"));
    }




    @Test
    public void testRegistrationWhileLoggedIn() {
        RegistrationTest.registerUserWithDefaulCredentials(selenium);
        LoginTest.loginUserWithDefaulCredentials(selenium);

        open("register.html");
        verifyTrue(selenium.getLocation().
                endsWith("error.html"));
    }
}
