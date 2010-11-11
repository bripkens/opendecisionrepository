package nl.rug.search.odr;

import org.junit.Test;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
public class DecisionWizardTest extends AbstractSelenseTestCase {

    public static final String TEXTUAL_ELEMENT_INPUT_1 = "//form[@id='manageDecisionForm']/div/div[1]/textarea";

    public static final String TEXTUAL_ELEMENT_INPUT_2 = "//form[@id='manageDecisionForm']/div/div[2]/textarea";

    public static final String TEXTUAL_ELEMENT_INPUT_3 = "//form[@id='manageDecisionForm']/div/div[3]/textarea";

    public static final String TEXTUAL_ELEMENT_INPUT_4 = "//form[@id='manageDecisionForm']/div/div[4]/textarea";




    private void prepareForTest() {
        addStakeholderRoles();
        addStates();
        addTemplates();
        addRelationshipTypess();

        RegistrationTest.registerUserWithDefaulCredentials(selenium);
        LoginTest.loginUserWithDefaulCredentials(selenium);
        ManageProjectTest.createDefaultProject(selenium);
    }




    @Test
    public void testNormalUsage() {
        prepareForTest();

        selenium.click("addDecisionWizardLink");
        waitForPageToLoad();

        verifyTrue(selenium.isTextPresent("Add decision"));
        verifyTrue(selenium.isTextPresent("Essentials"));
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



        verifyTrue(selenium.isTextPresent("Add decision"));
        verifyTrue(selenium.isTextPresent("Attributes"));
        selenium.type(TEXTUAL_ELEMENT_INPUT_1, "problem");
        selenium.click(TEXTUAL_ELEMENT_INPUT_2);
        selenium.type(TEXTUAL_ELEMENT_INPUT_2, "decision");
        selenium.click(TEXTUAL_ELEMENT_INPUT_3);
        selenium.type(TEXTUAL_ELEMENT_INPUT_3, "alternatives");
        selenium.click(TEXTUAL_ELEMENT_INPUT_4);
        selenium.type(TEXTUAL_ELEMENT_INPUT_4, "arguments");
        selenium.click("manageDecisionForm:nextStepButton");
        sleep(1000);



        verifyTrue(selenium.isTextPresent("Add decision"));
        verifyTrue(selenium.isTextPresent("State"));
        verifyTrue(selenium.isElementPresent("manageDecisionForm:inState")); // make sure formulated is selected
        selenium.select("manageDecisionForm:inState", "label=considered");
        verifyTrue(selenium.isVisible("stateChangeInformation"));
        verifyTrue(selenium.isTextPresent(RegistrationTest.EMAIL));
        selenium.click("//img[@alt='remove']");
        verifyTrue(selenium.isTextPresent("At least one initiator is required"));
        verifyTrue(selenium.isTextPresent(RegistrationTest.EMAIL));
        selenium.click("manageDecisionForm:nextStepButton");
        sleep(1000);




        verifyTrue(selenium.isTextPresent("Add decision"));
        verifyTrue(selenium.isTextPresent("Relationships"));
        verifyEquals("Please select", selenium.getText("manageDecisionForm:inRelationship"));
        selenium.click("manageDecisionForm:nextStepButton");
        sleep(1000);

        verifyTrue(selenium.isTextPresent("Add decision"));
        verifyTrue(selenium.isTextPresent("Confirmation"));
        verifyTrue(selenium.isTextPresent("Java Programming language"));
        verifyTrue(selenium.isTextPresent("considered"));
        verifyTrue(selenium.isTextPresent(RegistrationTest.USER_NAME));
        verifyTrue(selenium.isTextPresent("Viewpoints for architectural decisions"));
        verifyTrue(selenium.isTextPresent("problem"));
        verifyTrue(selenium.isTextPresent("decision"));
        verifyTrue(selenium.isTextPresent("alternatives"));
        verifyTrue(selenium.isTextPresent("arguments"));

        selenium.click("manageDecisionForm:manageDecisionSubmitButton");
        sleep(1000);
        verifyTrue(selenium.isTextPresent("Java Programming language"));
        verifyTrue(selenium.isTextPresent("considered"));

        open("p/" + ManageProjectTest.PROJECT_NAME);

        selenium.click("//form[@id='dicisionShowForm']/table[1]/tbody/tr[1]/td[3]/a[2]");
        sleep(1000);

        verifyTrue(selenium.isTextPresent("Update decision"));
        verifyTrue(selenium.isTextPresent("Essentials"));
        verifyEquals("Java Programming language", selenium.getValue("manageDecisionForm:inName"));
        verifyEquals("Viewpoints for architectural decisions", selenium.getSelectedLabel("manageDecisionForm:inTemplate"));
        verifyTrue(selenium.isElementPresent("manageDecisionForm:decisionDate:Month"));
        verifyTrue(selenium.isElementPresent("manageDecisionForm:decisionDate:Day"));
        verifyTrue(selenium.isElementPresent("manageDecisionForm:decisionDate:Year"));
        verifyTrue(selenium.isElementPresent("manageDecisionForm:decisionDate:Hour"));
        verifyTrue(selenium.isElementPresent("manageDecisionForm:decisionDate:Minute"));
        verifyEquals("", selenium.getValue("manageDecisionForm:inOprLink"));

        selenium.click("manageDecisionForm:nextStepButton");
        sleep(1000);
    }




    @Test
    public void testSecondStepSkipped() {
        prepareForTest();

        selenium.click("addDecisionWizardLink");
        waitForPageToLoad();

        verifyTrue(selenium.isTextPresent("Add decision"));
        verifyTrue(selenium.isTextPresent("Essentials"));
        selenium.type("manageDecisionForm:inName", "Java Programming language");
        selenium.click("manageDecisionForm:inTemplate");
        selenium.select("manageDecisionForm:inTemplate", "label=Quick add form");
        selenium.click("//option[@value='Viewpoints for architectural decisions']");
        selenium.click("manageDecisionForm:nextStepButton");
        sleep(1000);

        verifyTrue(selenium.isTextPresent("Add decision"));
        verifyTrue(selenium.isTextPresent("State"));
    }




    public void testTemplateDataMaintainedWhenChangingTemplate() {
        prepareForTest();

//        selenium.setSpeed("1000");

        selenium.click("addDecisionWizardLink");
        waitForPageToLoad();

        // step 1

        verifyTrue(selenium.isTextPresent("Add decision"));
        verifyTrue(selenium.isTextPresent("Essentials"));
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


        // step 2

        String inputFirstElement = "firstElementBlabladasdasdasda";
        String inputSecondElement = "secondElementFofofoffoofofof";
        String inputThirdElement = "thirdElementAdasdsadasdas";
        String inputFourthElement = "fourthElementabababaababa";

        verifyTrue(selenium.isTextPresent("Add decision"));
        verifyTrue(selenium.isTextPresent("Attributes"));
        selenium.type(TEXTUAL_ELEMENT_INPUT_1, inputFirstElement);
        selenium.click(TEXTUAL_ELEMENT_INPUT_2);
        selenium.type(TEXTUAL_ELEMENT_INPUT_2, inputSecondElement);
        selenium.click(TEXTUAL_ELEMENT_INPUT_3);
        selenium.type(TEXTUAL_ELEMENT_INPUT_3, inputThirdElement);
        selenium.click(TEXTUAL_ELEMENT_INPUT_4);
        selenium.type(TEXTUAL_ELEMENT_INPUT_4, inputFourthElement);
        selenium.click("manageDecisionForm:toLastStepButtonTop");
        sleep(1000);

        // step 5

        selenium.click("manageDecisionForm:manageDecisionSubmitButton");
        sleep(1000);

        // decision detail view

        open("p/" + ManageProjectTest.PROJECT_NAME);

        // project detail view

        selenium.click("//form[@id='dicisionShowForm']/table[1]/tbody/tr[1]/td[3]/a[2]");
        sleep(1000);

        // update decision step 1

        selenium.select("manageDecisionForm:inTemplate", "label=Demystifying architecture");
        selenium.click("//option[@value='Demystifying architecture']");
        selenium.click("manageDecisionForm:nextStepButton");
        sleep(1000);

        // step 2

        selenium.click("manageDecisionForm:toLastStepButtonTop");
        sleep(1000);

        // step 5

        selenium.click("manageDecisionForm:manageDecisionSubmitButton");
        sleep(1000);

        // decision detail view

        open("p/" + ManageProjectTest.PROJECT_NAME);

        // project detail view

        selenium.click("//form[@id='dicisionShowForm']/table[1]/tbody/tr[1]/td[3]/a[2]");
        sleep(1000);

        // step 1
        selenium.select("manageDecisionForm:inTemplate", "label=Viewpoints for architectural decisions");
        selenium.click("//option[@value='Viewpoints for architectural decisions']");
        selenium.click("manageDecisionForm:nextStepButton");
        sleep(1000);

        // step 2
        verifyEquals(inputFirstElement, selenium.getText(TEXTUAL_ELEMENT_INPUT_1));
        verifyEquals(inputSecondElement, selenium.getText(TEXTUAL_ELEMENT_INPUT_2));
        verifyEquals(inputThirdElement, selenium.getText(TEXTUAL_ELEMENT_INPUT_3));
        verifyEquals(inputFourthElement, selenium.getText(TEXTUAL_ELEMENT_INPUT_4));
    }
}
