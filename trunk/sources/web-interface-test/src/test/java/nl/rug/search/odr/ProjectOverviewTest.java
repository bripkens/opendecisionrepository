package nl.rug.search.odr;

import com.thoughtworks.selenium.Selenium;
import org.junit.Ignore;

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




    public void testQuickAddDecision() {
        RegistrationTest.registerUserWithDefaulCredentials(selenium);
        LoginTest.loginUserWithDefaulCredentials(selenium);

        addStakeholderRoles();
        addStates();
        addTemplates();

        ManageProjectTest.createDefaultProject(selenium);



        selenium.click("showQuickAddDecisionContainer");
        sleep(1000);
        verifyTrue(selenium.isVisible("decisionQuickAddContainer"));

        String decisionName = "FirstDecisionNamePlaceHolder";
        selenium.type("decisionQuickAddContainer:inDecisionName", decisionName);
        selenium.click("decisionQuickAddContainer:decisionAddSubmitButton");
        sleep(1000);

        verifyFalse(selenium.isVisible("decisionQuickAddContainer"));

        verifyTrue(selenium.isTextPresent(decisionName));

        selenium.click("showQuickAddDecisionContainer");
        sleep(1000);
        verifyTrue(selenium.isVisible("decisionQuickAddContainer"));

        selenium.type("decisionQuickAddContainer:inDecisionName", decisionName);
        selenium.click("decisionQuickAddContainer:decisionAddSubmitButton");
        waitForAjaxRequest();

        verifyTrue(selenium.isVisible("decisionQuickAddContainer"));
        verifyTrue(selenium.isTextPresent("Decision name already used"));
    }


    public void testQuickAddShowHide() {
        RegistrationTest.registerUserWithDefaulCredentials(selenium);
        LoginTest.loginUserWithDefaulCredentials(selenium);

        addStakeholderRoles();
        addStates();
        addTemplates();

        ManageProjectTest.createDefaultProject(selenium);

        selenium.click("showQuickAddDecisionContainer");
        sleep(1000);
        verifyTrue(selenium.isVisible("decisionQuickAddContainer"));
        selenium.click("showQuickAddDecisionContainer");
        sleep(1000);
        verifyFalse(selenium.isVisible("decisionQuickAddContainer"));
        selenium.click("showQuickAddDecisionContainer");
        sleep(1000);
        verifyTrue(selenium.isVisible("decisionQuickAddContainer"));
        selenium.click("decisionQuickAddContainer:decisionAddAbortButton");
        sleep(1000);
        verifyFalse(selenium.isVisible("decisionQuickAddContainer"));
    }
}
