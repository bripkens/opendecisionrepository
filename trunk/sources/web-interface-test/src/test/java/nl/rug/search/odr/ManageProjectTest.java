package nl.rug.search.odr;

import com.thoughtworks.selenium.Selenium;
import org.junit.Ignore;
import org.junit.Test;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
public class ManageProjectTest extends AbstractSelenseTestCase {

    public static final String PROJECT_NAME = "OpenDecisionRepository";

    public static final String PROJECT_DESCRIPTION = "Some project description.";

    public static final String OTHER_MEMBER = "jenni@jenni.de";

    public static final String USER_ROLE = "Architect";

    public static final String OTHER_MEMBER_ROLE = "Customer";


    public static final String FIRST_ROLE_SELECT_ID = "manageProjectForm:j_idt60:j_idt72";
    public static final String SECOND_ROLE_SELECT_ID = "manageProjectForm:j_idt60:0:j_idt75";
    public static final String THIRD_ROLE_SELECT_ID = "manageProjectForm:j_idt60:1:j_idt75";
    public static final String FIRST_REMOVE_LINK_ID = "manageProjectForm:j_idt60:0:j_idt78";

    public static void createDefaultProject(Selenium selenium) {
        createProject(selenium, PROJECT_NAME, PROJECT_DESCRIPTION, OTHER_MEMBER, USER_ROLE, OTHER_MEMBER_ROLE);
    }




    public static void createProject(Selenium selenium, String ProjectName, String ProjectDescription,
                                     String OtherMember, String UserRole, String OtherMemberRole) {
        open(selenium, "projects.html");
        selenium.click("link=here");
        waitForPageToLoad(selenium);

        sleep(1000);

        selenium.type("manageProjectForm:inName", PROJECT_NAME);
        selenium.type("manageProjectForm:inDescription", ProjectDescription);
        selenium.type("manageProjectForm:inMember", OtherMember);
        selenium.click("manageProjectForm:inMemberAdd");
        sleep(1000);
        selenium.select(FIRST_ROLE_SELECT_ID, "label=".concat(UserRole));
        selenium.select(SECOND_ROLE_SELECT_ID, "label=".concat(OtherMemberRole));
        selenium.click("manageProjectForm:manageProjectCreateButton");
        waitForPageToLoad(selenium);
    }



    @Test
    public void testCreateProject() {
        RegistrationTest.registerUserWithDefaulCredentials(selenium);
        LoginTest.loginUserWithDefaulCredentials(selenium);

        addStakeholderRoles();
        addStates();

        selenium.setSpeed("1000");

        createDefaultProject(selenium);

        sleep(1000);

        verifyTrue(selenium.isTextPresent(PROJECT_NAME));
        verifyTrue(selenium.isTextPresent(PROJECT_DESCRIPTION));
        verifyTrue(selenium.isTextPresent(RegistrationTest.USER_NAME));
        verifyTrue(selenium.isTextPresent(RegistrationTest.EMAIL));
        verifyTrue(selenium.isTextPresent(OTHER_MEMBER));
        verifyTrue(selenium.isTextPresent(USER_ROLE));
        verifyTrue(selenium.isTextPresent(OTHER_MEMBER_ROLE));
    }

    @Test
    public void testUpdateProject() {
        RegistrationTest.registerUserWithDefaulCredentials(selenium);
        LoginTest.loginUserWithDefaulCredentials(selenium);

        addStakeholderRoles();
        addStates();

        createDefaultProject(selenium);

        sleep(1000);

        selenium.click("//img[@alt='edit']");
        waitForPageToLoad();

        sleep(1000);

        verifyEquals(PROJECT_NAME, selenium.getValue("manageProjectForm:inName"));
        verifyEquals(PROJECT_DESCRIPTION, selenium.getText("manageProjectForm:inDescription"));
        verifyTrue(selenium.isTextPresent(RegistrationTest.USER_NAME));
        verifyTrue(selenium.isTextPresent(RegistrationTest.EMAIL));
        verifyTrue(selenium.isTextPresent(OTHER_MEMBER));
        verifyEquals(USER_ROLE, selenium.getSelectedLabel(FIRST_ROLE_SELECT_ID));
        verifyEquals(OTHER_MEMBER_ROLE, selenium.getSelectedLabel(SECOND_ROLE_SELECT_ID));

        String newName = "FancyNewName";
        String newDescription = "Some other description";
        String newMember = "peter@peter.de";
        String newMemberRole = "Manager";

        selenium.type("manageProjectForm:inName", newName);
        selenium.type("manageProjectForm:inDescription", newDescription);
        selenium.type("manageProjectForm:inMember", newMember);
        selenium.click("manageProjectForm:inMemberAdd");
        waitForAjaxRequest();
        selenium.select(THIRD_ROLE_SELECT_ID, "label=".concat(newMemberRole));
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
        LoginTest.loginUserWithDefaulCredentials(selenium);

        addStakeholderRoles();
        addStates();

        createDefaultProject(selenium);

        selenium.click("//img[@alt='remove']");
        waitForPageToLoad();
        selenium.click("deleteConfirmForm:confirmDeleteButton");
        waitForPageToLoad();
        verifyTrue(selenium.isTextPresent("You have no projects"));
        verifyTrue(selenium.isTextPresent("Your projects"));
    }

    @Test
    public void testNoRightsForProject() {
        RegistrationTest.registerUserWithDefaulCredentials(selenium);
        RegistrationTest.registerUser(selenium, "passw", "stefan", "stefan@stefan.de");

        LoginTest.loginUserWithDefaulCredentials(selenium);

        addStakeholderRoles();
        addStates();

        createDefaultProject(selenium);

        verifyTrue(selenium.isTextPresent("Some project description."));

        LoginTest.logout(selenium);

        LoginTest.loginUser(selenium, "stefan@stefan.de", "passw");
        verifyFalse(selenium.isTextPresent(ManageProjectTest.PROJECT_NAME));
    }

    @Test
    public void testRemoveMemberFromProject() {
        RegistrationTest.registerUserWithDefaulCredentials(selenium);
        LoginTest.loginUserWithDefaulCredentials(selenium);

        addStakeholderRoles();
        addStates();

        createDefaultProject(selenium);

        selenium.click("//img[@alt='edit']");
        waitForPageToLoad();
        waitForAjaxRequest();

        selenium.click(FIRST_REMOVE_LINK_ID);
        waitForAjaxRequest();

        selenium.click("manageProjectForm:manageProjectCreateButton");
        waitForPageToLoad();

        verifyFalse(selenium.isTextPresent(OTHER_MEMBER));

        selenium.click("//img[@alt='edit']");
        waitForPageToLoad();
        waitForAjaxRequest();

        selenium.type("manageProjectForm:inMember", OTHER_MEMBER);
        selenium.click("manageProjectForm:inMemberAdd");
        waitForAjaxRequest();
        verifyEquals(OTHER_MEMBER_ROLE, selenium.getSelectedLabel(SECOND_ROLE_SELECT_ID));
    }
}
