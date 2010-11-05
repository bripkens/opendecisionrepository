package nl.rug.search.odr;
import org.junit.Test;
/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
public class DecisionWizardTest extends AbstractSelenseTestCase {

    private void prepareForTest() {
        addStakeholderRoles();
        addStates();
        addTemplates();
        addRelationships();

        RegistrationTest.registerUserWithDefaulCredentials(selenium);
        LoginTest.loginUserWithDefaulCredentials(selenium);
        ManageProjectTest.createDefaultProject(selenium);
    }




    @Test
    public void testNormalUsage() {
        prepareForTest();

        selenium.click("addDecisionWizardLink");
        selenium.waitForPageToLoad("30000");
        selenium.type("manageDecisionForm:inName", "Java Programming language");
        selenium.click("manageDecisionForm:inTemplate");
        selenium.select("manageDecisionForm:inTemplate", "label=Viewpoints for architectural decisions");
        selenium.click("//option[@value='Viewpoints for architectural decisions']");
        verifyTrue(selenium.isVisible("templateChangeInformation"));
        verifyTrue(selenium.isElementPresent("manageDecisionForm:decisionDate:Month")); // check current date
        verifyTrue(selenium.isElementPresent("manageDecisionForm:decisionDate:Day"));
        verifyTrue(selenium.isElementPresent("manageDecisionForm:decisionDate:Year"));
        verifyTrue(selenium.isElementPresent("manageDecisionForm:decisionDate:Hour"));
        verifyTrue(selenium.isElementPresent("manageDecisionForm:decisionDate:Minute"));
        selenium.click("manageDecisionForm:nextStepButton");
        sleep(1000);




        selenium.type("manageDecisionForm:j_idt158:0:j_idt161", "problem");
        selenium.click("manageDecisionForm:j_idt158:1:j_idt161");
        selenium.type("manageDecisionForm:j_idt158:1:j_idt161", "decision");
        selenium.click("manageDecisionForm:j_idt158:2:j_idt161");
        selenium.type("manageDecisionForm:j_idt158:2:j_idt161", "alternatives");
        selenium.click("manageDecisionForm:j_idt158:3:j_idt161");
        selenium.type("manageDecisionForm:j_idt158:3:j_idt161", "arguments");
        selenium.click("manageDecisionForm:nextStepButton");
        sleep(1000);



        verifyTrue(selenium.isElementPresent("manageDecisionForm:inState")); // make sure formulated is selected
        selenium.select("manageDecisionForm:inState", "label=considered");
        verifyTrue(selenium.isVisible("stateChangeInformation"));
        verifyTrue(selenium.isTextPresent(RegistrationTest.EMAIL));
        selenium.click("//img[@alt='remove']");
        verifyTrue(selenium.isTextPresent("At least one initiator is required"));
        verifyTrue(selenium.isTextPresent(RegistrationTest.EMAIL));
        selenium.click("manageDecisionForm:nextStepButton");
        sleep(1000);





        verifyEquals("Please select", selenium.getText("manageDecisionForm:inRelationship"));
        selenium.click("manageDecisionForm:nextStepButton");
        sleep(1000);



        verifyTrue(selenium.isTextPresent("Java Programming language"));
        verifyTrue(selenium.isTextPresent("considered"));
        verifyTrue(selenium.isTextPresent("Ben Ripkens"));
        verifyTrue(selenium.isTextPresent("Viewpoints for architectural decisions"));
        verifyTrue(selenium.isTextPresent("problem"));
        verifyTrue(selenium.isTextPresent("decision"));
        verifyTrue(selenium.isTextPresent("alternatives"));
        verifyTrue(selenium.isTextPresent("arguments"));

        selenium.click("manageDecisionForm:manageDecisionSubmitButton");
        sleep(1000);
        verifyTrue(selenium.isTextPresent("Java Programming language"));
        verifyTrue(selenium.isTextPresent("considered"));

    }
}
