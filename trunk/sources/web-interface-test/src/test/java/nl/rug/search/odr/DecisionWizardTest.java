package nl.rug.search.odr;

import com.thoughtworks.selenium.Selenium;
import org.junit.Ignore;
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
    public static final String TEXTUAL_ELEMENT_INPUT_5 = "//form[@id='manageDecisionForm']/div/div[5]/textarea";
    public static final String TEXTUAL_ELEMENT_INPUT_6 = "//form[@id='manageDecisionForm']/div/div[6]/textarea";
    public static final String TEXTUAL_ELEMENT_INPUT_7 = "//form[@id='manageDecisionForm']/div/div[7]/textarea";
    public static final String TEXTUAL_ELEMENT_INPUT_8 = "//form[@id='manageDecisionForm']/div/div[8]/textarea";
    public static final String TEXTUAL_ELEMENT_INPUT_9 = "//form[@id='manageDecisionForm']/div/div[9]/textarea";
    public static final String InputText1000 = "Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam "
                                               + "nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos "
                                               + "et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est "
                                               + "Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy "
                                               + "eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et "
                                               + "accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem "
                                               + "ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod "
                                               + "tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et "
                                               + "justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor "
                                               + "sit amet.Duis autem vel eum iriure dolor in hendrerit in vulputate velit esse molestie consequat, vel "
                                               + "illum dolore eu f";




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




    @Test
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




    @Test
    public void testNoTemplateOrName() {
        prepareForTest();

        selenium.click("addDecisionWizardLink");
        waitForPageToLoad();

        // step 1
        verifyTrue(selenium.isTextPresent("Add decision"));
        verifyTrue(selenium.isTextPresent("Essentials"));
        selenium.fireEvent("manageDecisionForm:inOprLink", "blur");
        selenium.click("manageDecisionForm:toLastStepButtonTop");
        waitForAjaxRequest();
        verifyTrue(selenium.isTextPresent("Name: Is required"));
        selenium.type("manageDecisionForm:inName", "a");
        waitForAjaxRequest();
        selenium.fireEvent("manageDecisionForm:inName", "blur");
        waitForAjaxRequest();
        verifyTrue(selenium.isTextPresent("Has less than allowed 3 chars"));
        verifyTrue(selenium.isTextPresent("Template: Please select a value"));
    }




    @Test
    public void testRelationshipsFuture() {
        prepareForTest();
        testQuickAdd("Java Server Faces 2.0");
        testQuickAdd("Java Server Faces 1.8");
        testQuickAdd("Ice Faces 2.0");
        testQuickAdd("Java Enterprise");
        testQuickAdd("MySQL");

        selenium.click("addDecisionWizardLink");
        waitForPageToLoad();

        verifyTrue(selenium.isTextPresent("Add decision"));
        verifyTrue(selenium.isTextPresent("Essentials"));
        selenium.type("manageDecisionForm:inName", "Derby");
        selenium.click("manageDecisionForm:inTemplate");
        selenium.select("manageDecisionForm:inTemplate", "label=Viewpoints for architectural decisions");
        selenium.click("//option[@value='Viewpoints for architectural decisions']");
        verifyTrue(selenium.isVisible("templateChangeInformation"));
        verifyTrue(selenium.isElementPresent("manageDecisionForm:decisionDate:Month")); // check current date
        verifyTrue(selenium.isElementPresent("manageDecisionForm:decisionDate:Day"));
        verifyTrue(selenium.isElementPresent("manageDecisionForm:decisionDate:Year"));
        selenium.select("manageDecisionForm:decisionDate:Year", "label=1997");
        verifyTrue(selenium.isElementPresent("manageDecisionForm:decisionDate:Hour"));
        verifyTrue(selenium.isElementPresent("manageDecisionForm:decisionDate:Minute"));
        selenium.click("manageDecisionForm:nextStepButton");
        sleep(1000);

//step2

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

//step3

        verifyTrue(selenium.isTextPresent("Add decision"));
        verifyTrue(selenium.isTextPresent("State"));
        verifyTrue(selenium.isElementPresent("manageDecisionForm:inState")); // make sure formulated is selected
        selenium.select("manageDecisionForm:inState", "label=considered");
        verifyTrue(selenium.isVisible("stateChangeInformation"));
        verifyTrue(selenium.isTextPresent(RegistrationTest.EMAIL));
        selenium.click("//img[@alt='remove']");
        waitForAjaxRequest();
        verifyTrue(selenium.isTextPresent("At least one initiator is required"));
        verifyTrue(selenium.isTextPresent(RegistrationTest.EMAIL));
        selenium.click("manageDecisionForm:nextStepButton");
        sleep(1000);

//step4

        verifyTrue(selenium.isTextPresent("Add decision"));
        verifyTrue(selenium.isTextPresent("Relationships"));
        selenium.select("manageDecisionForm:inRelationship", "label=MySQL [decided after]");
        selenium.click("manageDecisionForm:manageDecisionRelationshipAddButton");
        verifyTrue(selenium.isTextPresent("The Open Decision Repository allows relationships to decisions which have been made after this decision although this is logically impossible. You will still be able to use the whole ODR functionality but note that such documentation is confusing. "));
        verifyTrue(selenium.isTextPresent("[decided after]"));

        selenium.select("//form[@id='manageDecisionForm']/div[2]/div[1]/select[1]", "label=is alternative for");

        selenium.select("manageDecisionForm:inRelationship", "label=Java Server Faces 2.0 [decided after]");
        selenium.click("manageDecisionForm:manageDecisionRelationshipAddButton");
        sleep(500);
        waitForAjaxRequest();
        selenium.select("//form[@id='manageDecisionForm']/div[2]/div[2]/select[1]", "label=caused by");


        selenium.click("manageDecisionForm:nextStepButton");
        sleep(1000);

        verifyTrue(selenium.isTextPresent("Add decision"));
        verifyTrue(selenium.isTextPresent("Confirmation"));
        verifyTrue(selenium.isTextPresent("Derby"));
        verifyTrue(selenium.isTextPresent("considered"));
        verifyTrue(selenium.isTextPresent(RegistrationTest.USER_NAME));
        verifyTrue(selenium.isTextPresent("Viewpoints for architectural decisions"));
        verifyTrue(selenium.isTextPresent("problem"));
        verifyTrue(selenium.isTextPresent("decision"));
        verifyTrue(selenium.isTextPresent("alternatives"));
        verifyTrue(selenium.isTextPresent("arguments"));
        verifyTrue(selenium.isTextPresent("Derby <<is alternative for>> MySQL"));
        verifyTrue(selenium.isTextPresent("Derby <<caused by>> Java Server Faces 2.0"));

        selenium.click("manageDecisionForm:manageDecisionSubmitButton");
        sleep(1000);
        verifyTrue(selenium.isTextPresent("Derby"));
        verifyTrue(selenium.isTextPresent("Problem/Issue"));
        verifyTrue(selenium.isTextPresent("problem"));
        verifyTrue(selenium.isTextPresent("Decision"));
        verifyTrue(selenium.isTextPresent("decision"));
        verifyTrue(selenium.isTextPresent("Alternatives"));
        verifyTrue(selenium.isTextPresent("alternatives"));
        verifyTrue(selenium.isTextPresent("Arguments"));
        verifyTrue(selenium.isTextPresent("arguments"));
        verifyTrue(selenium.isTextPresent("Contents"));

        verifyTrue(selenium.isTextPresent("Abstract"));
        verifyTrue(selenium.isTextPresent("State: considered"));
        verifyTrue(selenium.isTextPresent("Ben Ripkens <ben@ripkens.de>"));
        verifyTrue(selenium.isTextPresent("Template: Viewpoints for architectural decisions"));
        verifyTrue(selenium.isTextPresent("Requirements"));
        verifyTrue(selenium.isTextPresent("<<is alternative for>> MySQL "));
        verifyTrue(selenium.isTextPresent("<<caused by>> Java Server Faces 2.0 "));
        verifyTrue(selenium.isTextPresent("History"));

        selenium.click("//img[@alt='edit']");
        sleep(1000);




        //UPDATE THE PROJECT


        verifyTrue(selenium.isTextPresent("Add decision"));
        verifyTrue(selenium.isTextPresent("Essentials"));
        selenium.type("manageDecisionForm:inName", "Derby");
        selenium.click("manageDecisionForm:inTemplate");
        selenium.select("manageDecisionForm:inTemplate", "label=Demystifying architecture");
        selenium.click("//option[@value='Demystifying architecture']");
        verifyTrue(selenium.isVisible("templateChangeInformation"));
        verifyTrue(selenium.isElementPresent("manageDecisionForm:decisionDate:Month")); // check current date
        verifyTrue(selenium.isElementPresent("manageDecisionForm:decisionDate:Day"));
        verifyTrue(selenium.isElementPresent("manageDecisionForm:decisionDate:Year"));
        selenium.select("manageDecisionForm:decisionDate:Year", "label=1998");
        verifyTrue(selenium.isElementPresent("manageDecisionForm:decisionDate:Hour"));
        verifyTrue(selenium.isElementPresent("manageDecisionForm:decisionDate:Minute"));
        selenium.click("manageDecisionForm:nextStepButton");
        sleep(1000);

//step2

        verifyTrue(selenium.isTextPresent("Add decision"));
        verifyTrue(selenium.isTextPresent("Attributes"));
        selenium.type(TEXTUAL_ELEMENT_INPUT_1, "Issue update");
        selenium.click(TEXTUAL_ELEMENT_INPUT_2);
        selenium.type(TEXTUAL_ELEMENT_INPUT_2, "decision update");
        selenium.click(TEXTUAL_ELEMENT_INPUT_3);
        selenium.type(TEXTUAL_ELEMENT_INPUT_3, "group update");
        selenium.click(TEXTUAL_ELEMENT_INPUT_4);
        selenium.type(TEXTUAL_ELEMENT_INPUT_4, "assumption update");
        selenium.click(TEXTUAL_ELEMENT_INPUT_5);
        selenium.type(TEXTUAL_ELEMENT_INPUT_5, "Constraints update");
        selenium.click(TEXTUAL_ELEMENT_INPUT_6);
        selenium.type(TEXTUAL_ELEMENT_INPUT_6, "positions update");
        selenium.click(TEXTUAL_ELEMENT_INPUT_7);
        selenium.type(TEXTUAL_ELEMENT_INPUT_7, "arguments update");
        selenium.click(TEXTUAL_ELEMENT_INPUT_8);
        selenium.type(TEXTUAL_ELEMENT_INPUT_8, "implications update");
        selenium.click(TEXTUAL_ELEMENT_INPUT_9);
        selenium.type(TEXTUAL_ELEMENT_INPUT_9, "notes update");
        selenium.click("manageDecisionForm:nextStepButton");
        sleep(1000);

//step3

        verifyTrue(selenium.isTextPresent("Add decision"));
        verifyTrue(selenium.isTextPresent("State"));
        verifyTrue(selenium.isElementPresent("manageDecisionForm:inState")); // make sure formulated is selected
        selenium.select("manageDecisionForm:inState", "label=decided");
        verifyTrue(selenium.isVisible("stateChangeInformation"));
        verifyTrue(selenium.isTextPresent(RegistrationTest.EMAIL));
        selenium.select("manageDecisionForm:inInitiators", "label=jenni@jenni.de");
        selenium.click("manageDecisionForm:manageDecisionInitiatorAddButton");
        selenium.click("manageDecisionForm:nextStepButton");
        sleep(1000);

//step4

        verifyTrue(selenium.isTextPresent("Add decision"));
        verifyTrue(selenium.isTextPresent("Relationships"));
        verifyEquals("Please select", selenium.getText("manageDecisionForm:inRelationship"));
        selenium.click("//form[@id='manageDecisionForm']/div[2]/div[1]/a[1]/img");
        waitForAjaxRequest();
        verifyFalse(selenium.isTextPresent("MySQL"));

        selenium.click("manageDecisionForm:nextStepButton");
        sleep(1000);

        selenium.click("manageDecisionForm:manageDecisionSubmitButton");
        sleep(1000);

        verifyTrue(selenium.isTextPresent("Derby"));
        verifyTrue(selenium.isTextPresent("Issue update"));
        verifyTrue(selenium.isTextPresent("decision update"));
        verifyTrue(selenium.isTextPresent("group update"));
        verifyTrue(selenium.isTextPresent("assumption update"));
        verifyTrue(selenium.isTextPresent("Constraints update"));
        verifyTrue(selenium.isTextPresent("positions update"));
        verifyTrue(selenium.isTextPresent("arguments update"));
        verifyTrue(selenium.isTextPresent("implications update"));

        verifyTrue(selenium.isTextPresent("Abstract"));
        verifyTrue(selenium.isTextPresent("State: decided"));
        verifyTrue(selenium.isTextPresent("jenni@jenni.de"));
        verifyTrue(selenium.isTextPresent("Template: Demystifying architecture"));
        verifyTrue(selenium.isTextPresent("Requirements"));
        verifyTrue(selenium.isTextPresent("<<is alternative for>><caused by Java Server Faces 2.0 "));
        verifyTrue(selenium.isTextPresent("History"));
    }




    public void testQuickAdd(String decisionName) {
        selenium.click("showQuickAddDecisionContainer");
        sleep(1000);
        verifyTrue(selenium.isVisible("decisionQuickAddContainer"));

        selenium.type("decisionQuickAddContainer:inDecisionName", decisionName);
        selenium.click("decisionQuickAddContainer:decisionAddSubmitButton");
        sleep(1000);
    }
}
