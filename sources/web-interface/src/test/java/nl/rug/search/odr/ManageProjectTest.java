package nl.rug.search.org;

import com.thoughtworks.selenium.SeleneseTestCase;
import com.thoughtworks.selenium.Wait;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
public class ManageProjectTest extends SeleneseTestCase {

    @Override
    public void setUp() throws Exception {
        setUp("http://localhost:8080/web-interface/");
    }

    public void testProject() {
        selenium.open("/web-interface/");
        selenium.click("loginForm:registerLink");
        selenium.waitForPageToLoad("30000");
        selenium.type("registerForm:inName", "jenni");
        selenium.type("registerForm:inPassword", "12345");
        selenium.type("registerForm:inEmail", "jenni@jenni.de");
        selenium.click("registerForm:registrationSubmitButton");
        selenium.type("loginForm:inLoginEmail", "jenni@jenni.de");
        selenium.type("loginForm:inLoginPassword", "12345");
        selenium.click("loginForm:loginButton");
        selenium.waitForPageToLoad("30000");
        selenium.click("link=here");
        selenium.waitForPageToLoad("30000");
        selenium.type("registerForm:inName", "uni-duesseldorf");
        selenium.type("registerForm:inDescription", "somewhere in germany");
        selenium.type("registerForm:inMember", "ben");
        selenium.type("registerForm:inMember", "ben@ben.de");
        selenium.click("registerForm:inMemberAdd");

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

        selenium.click("registerForm:manageProjectCreateButton");
        selenium.waitForPageToLoad("30000");
        verifyTrue(selenium.isTextPresent("ni-duesseldor"));
        verifyTrue(selenium.isTextPresent("omewhere in german"));
        selenium.click("//img[@alt='edit']");
        selenium.waitForPageToLoad("30000");
        selenium.type("registerForm:inDescription", "somewhere in groningen");
        selenium.click("registerForm:manageProjectCreateButton");
        selenium.waitForPageToLoad("30000");
        verifyTrue(selenium.isTextPresent("groningen"));
        selenium.click("//img[@alt='delete']");
        selenium.waitForPageToLoad("30000");
        selenium.click("deleteConfirmForm:confirmDeleteButton");
        selenium.waitForPageToLoad("30000");
        verifyTrue(selenium.isTextPresent("u have no projects"));
        selenium.click("logoutForm:logoutButton");
        selenium.waitForPageToLoad("30000");
    }
}
