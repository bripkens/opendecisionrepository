package nl.rug.search.odr;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
public class ProjectOverviewTest extends AbstractSelenseTestCase {

    public void testNoProject() {
        RegistrationTest.registerUserWithDefaulCredentials(selenium);
        LoginTest.loginUserWithDefaulCredentials(selenium);
        verifyTrue(selenium.isTextPresent("You have no projects"));
        verifyTrue(selenium.isTextPresent("Your projects"));
    }




    public void testAccessProjectsWhileNotLoggedIn() {
        open("projects.html");
        verifyTrue(selenium.isTextPresent("Registration"));
        verifyFalse(selenium.isTextPresent("Your projects"));
    }



}
