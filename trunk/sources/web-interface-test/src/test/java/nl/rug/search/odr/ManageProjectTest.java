package nl.rug.search.odr;

import com.thoughtworks.selenium.Selenium;
import org.junit.Test;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
public class ManageProjectTest extends AbstractSelenseTestCase {

    public static final String PROJECT_NAME = "ODR";

    public static final String PROJECT_DESCRIPTION = "Some project description.";

    public static final String OTHER_MEMBER = "jenni@jenni.de";

    public static final String USER_ROLE = "Architect";

    public static final String OTHER_MEMBER_ROLE = "Customer";




    public static void createDefaultProject(Selenium selenium) {
        createProject(selenium, PROJECT_NAME, PROJECT_DESCRIPTION, OTHER_MEMBER, USER_ROLE, OTHER_MEMBER_ROLE);
    }




    public static void createProject(Selenium selenium, String ProjectName, String ProjectDescription,
                                     String OtherMember, String UserRole, String OtherMemberRole) {
        open(selenium, "projects.html");
        selenium.click("link=here");
        waitForPageToLoad(selenium);

        waitForAjaxRequest();

        selenium.type("manageProjectForm:inName", PROJECT_NAME);
        selenium.type("manageProjectForm:inDescription", ProjectDescription);
        selenium.type("manageProjectForm:inMember", OtherMember);
        selenium.click("manageProjectForm:inMemberAdd");
        waitForAjaxRequest();
        selenium.select("manageProjectForm:j_idt60:j_idt72", "label=".concat(UserRole));
        selenium.select("manageProjectForm:j_idt60:0:j_idt75", "label=".concat(OtherMemberRole));
        selenium.click("manageProjectForm:manageProjectCreateButton");
        waitForPageToLoad(selenium);
    }



//    @Test
//    public void testCreateProject() {
//        RegistrationTest.registerUserWithDefaulCredentials(selenium);
//        LoginTest.loginUserWithDefaulCredentials(selenium);
//
//        addStakeholderRoles();
//
//        createDefaultProject(selenium);
//
//        verifyTrue(selenium.isTextPresent(PROJECT_NAME));
//        verifyTrue(selenium.isTextPresent(PROJECT_DESCRIPTION));
//        verifyTrue(selenium.isTextPresent(RegistrationTest.USER_NAME));
//        verifyTrue(selenium.isTextPresent(RegistrationTest.EMAIL));
//        verifyTrue(selenium.isTextPresent(OTHER_MEMBER));
//        verifyTrue(selenium.isTextPresent(USER_ROLE));
//        verifyTrue(selenium.isTextPresent(OTHER_MEMBER_ROLE));
//    }

//    @Test
//    public void testUpdateProject() {
//        RegistrationTest.registerUserWithDefaulCredentials(selenium);
//        LoginTest.loginUserWithDefaulCredentials(selenium);
//
//        addStakeholderRoles();
//
//        createDefaultProject(selenium);
//
//        selenium.click("//img[@alt='edit']");
//        waitForPageToLoad();
//
//        sleep(1000);
//
//        verifyEquals(PROJECT_NAME, selenium.getValue("manageProjectForm:inName"));
//        verifyEquals(PROJECT_DESCRIPTION, selenium.getText("manageProjectForm:inDescription"));
//        verifyTrue(selenium.isTextPresent(RegistrationTest.USER_NAME));
//        verifyTrue(selenium.isTextPresent(RegistrationTest.EMAIL));
//        verifyTrue(selenium.isTextPresent(OTHER_MEMBER));
//        verifyEquals(USER_ROLE, selenium.getSelectedLabel("manageProjectForm:j_idt62:j_idt74"));
//        verifyEquals(OTHER_MEMBER_ROLE, selenium.getSelectedLabel("manageProjectForm:j_idt62:0:j_idt77"));
//
//        String newName = "FancyNewName";
//        String newDescription = "Some other description";
//        String newMember = "peter@peter.de";
//        String newMemberRole = "Manager";
//
//        selenium.type("manageProjectForm:inName", newName);
//        selenium.type("manageProjectForm:inDescription", newDescription);
//        selenium.type("manageProjectForm:inMember", newMember);
//        selenium.click("manageProjectForm:inMemberAdd");
//        waitForAjaxRequest();
//        selenium.select("manageProjectForm:j_idt60:1:j_idt75", "label=".concat(newMemberRole));
//        selenium.click("manageProjectForm:manageProjectCreateButton");
//        waitForPageToLoad();
//
//        verifyTrue(selenium.isTextPresent(newName));
//        verifyTrue(selenium.isTextPresent(newDescription));
//        verifyTrue(selenium.isTextPresent(RegistrationTest.USER_NAME));
//        verifyTrue(selenium.isTextPresent(RegistrationTest.EMAIL));
//        verifyTrue(selenium.isTextPresent(OTHER_MEMBER));
//        verifyTrue(selenium.isTextPresent(USER_ROLE));
//        verifyTrue(selenium.isTextPresent(OTHER_MEMBER_ROLE));
//        verifyTrue(selenium.isTextPresent(newMemberRole));
//        verifyTrue(selenium.isTextPresent(newMember));
//    }
//
//
//    @Test
//    public void testDeleteProject() {
//
//        RegistrationTest.registerUserWithDefaulCredentials(selenium);
//        LoginTest.loginUserWithDefaulCredentials(selenium);
//
//        addStakeholderRoles();
//
//        createDefaultProject(selenium);
//
//        selenium.click("//img[@alt='remove']");
//        waitForPageToLoad();
//        selenium.click("deleteConfirmForm:confirmDeleteButton");
//        waitForPageToLoad();
//        verifyTrue(selenium.isTextPresent("You have no projects"));
//        verifyTrue(selenium.isTextPresent("Your projects"));
//    }

    @Test
<<<<<<< .mine
    public void testNoRightsForProject() {
=======
    public void testCreateProject() {
>>>>>>> .r472
        RegistrationTest.registerUserWithDefaulCredentials(selenium);
        RegistrationTest.registerUser(selenium, "passw", "stefan", "stefan@stefan.de");

        LoginTest.loginUserWithDefaulCredentials(selenium);

        addStakeholderRoles();

        createDefaultProject(selenium);

        verifyTrue(selenium.isTextPresent("Some project description."));

        LoginTest.logout(selenium);


        LoginTest.loginUser(selenium, "stefan@stefan.de", "passw");
        verifyFalse(selenium.isTextPresent("ODR"));

<<<<<<< .mine
        open("p/ODR");
        verifyTrue(selenium.isTextPresent("You are not a member of this project and are therefore not allowed to access this project."));
        LoginTest.logout(selenium);
=======
        selenium.type("manageProjectForm:inName", newName);
        selenium.type("manageProjectForm:inDescription", newDescription);
        selenium.type("manageProjectForm:inMember", newMember);
        selenium.click("manageProjectForm:inMemberAdd");
        waitForAjaxRequest();
        selenium.select("manageProjectForm:j_idt62:1:j_idt77", "label=".concat(newMemberRole));
        selenium.click("manageProjectForm:manageProjectCreateButton");
        waitForPageToLoad();

        verifyTrue(selenium.isTextPresent(newName));
        verifyTrue(selenium.isTextPresent(newDescription));
        verifyTrue(selenium.isTextPresent(RegistrationTest.USER_NAME));
        verifyTrue(selenium.isTextPresent(RegistrationTest.EMAIL));
        verifyTrue(selenium.isTextPresent(OTHER_MEMBER));
        verifyTrue(selenium.isTextPresent(USER_ROLE));
        verifyTrue(selenium.isTextPresent(OTHER_MEMBER_ROLE));
        verifyTrue(selenium.isTextPresent(newMemberRole));
        verifyTrue(selenium.isTextPresent(newMember));
    }




    @Test
    public void testDeleteProject() {
        RegistrationTest.registerUserWithDefaulCredentials(selenium);
>>>>>>> .r472
        LoginTest.loginUserWithDefaulCredentials(selenium);
//        open("OPR/update");
//        verifyTrue(selenium.isTextPresent("no project member"));
//        open("OPR/delete");
//        verifyTrue(selenium.isTextPresent("no project member"));

        ManageIterationTest.createDefaultIteration(selenium, "ODR");
        sleep(30000);

    }
}
