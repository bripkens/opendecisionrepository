package nl.rug.search.odr;

import com.thoughtworks.selenium.Selenium;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;

/**
 *
 * @author Stefan Arians
 */
public class ManageConcernTest extends AbstractSelenseTestCase {

    private static final String concernId = "Concern-1";
    private static final String concernName = "First-concern";
    private static final String description = "description";
    private static final String tag1 = "test-tag";
    private static final String tag2 = "test-tag2";
    private ArrayList<String> tags = new ArrayList<String>();
    //START DATE IDS
    public static final String TAG_PATH_1 = "//form[@id='concernForm']/div[1]/div[4]/div[1]/input[1]";
    public static final String TAG_PATH_2 = "//form[@id='concernForm']/div[1]/div[4]/div[1]/input[2]";
    public static final String TAG_PATH_3 = "//form[@id='concernForm']/div[1]/div[4]/div[1]/input[3]";
    public static final String TAG_PATH_4 = "//form[@id='concernForm']/div[1]/div[4]/div[1]/input[4]";




    public static void createDefaultConcern(Selenium selenium) {
        List<String> tags = new ArrayList<String>();
        tags.add(tag1);
        tags.add(tag2);

        createConcern(selenium, concernId, concernName, description, tags);
        selenium.click("concernForm:submit");
        waitForAjaxRequest();
    }




    public static void createConcern(Selenium selenium, String concernId, String name, String desc, List<String> tags) {

        selenium.click("addConcernLink");
        waitForPageToLoad(selenium);

        //INFORMATIONS
        selenium.type("concernForm:inExternalId", concernId);
        selenium.type("concernForm:inName", name);
        selenium.type("concernForm:inDescription", desc);

        //Tags
        for (int i = 0; i < tags.size(); i++) {
            int j = i + 1;
            selenium.type("//form[@id='concernForm']/div[1]/div[4]/div[" + j + "]/input[1]", tags.get(i));
            waitForAjaxRequest();
        }
        //SUBMIT
        selenium.fireEvent("concernForm:inName", "blur");
        waitForAjaxRequest();
    }



    @Test
    public void testcreateConcern() {
        RegistrationTest.registerUserWithDefaulCredentials(selenium);
        LoginTest.loginUserWithDefaulCredentials(selenium);
        addAll();

        ManageProjectTest.createDefaultProject(selenium);
        open("p/".concat(ManageProjectTest.PROJECT_NAME));
        ManageConcernTest.createDefaultConcern(selenium);
        waitForAjaxRequest();
        verifyTrue(selenium.isTextPresent(concernId));
        verifyTrue(selenium.isTextPresent(concernName));
        verifyTrue(selenium.isTextPresent(description));
        verifyTrue(selenium.isTextPresent(tag1));
        verifyTrue(selenium.isTextPresent(tag2));
    }

    @Test
    public void testNoId() {
        RegistrationTest.registerUserWithDefaulCredentials(selenium);
        LoginTest.loginUserWithDefaulCredentials(selenium);
        addAll();

        ManageProjectTest.createDefaultProject(selenium);
        open("p/".concat(ManageProjectTest.PROJECT_NAME));
        ManageConcernTest.createConcern(selenium, "", "name", "desc", tags);
        selenium.click("concernForm:submit");
        waitForAjaxRequest();
        verifyTrue(selenium.isTextPresent("ID: Has less than allowed 1 chars"));
    }


    @Test
    public void testNoName() {
        RegistrationTest.registerUserWithDefaulCredentials(selenium);
        LoginTest.loginUserWithDefaulCredentials(selenium);
        addAll();

        ManageProjectTest.createDefaultProject(selenium);
        open("p/".concat(ManageProjectTest.PROJECT_NAME));
        selenium.setSpeed("2000");
        ManageConcernTest.createConcern(selenium, "concern-1", "", "desc", tags);
        selenium.click("concernForm:submit");
        waitForAjaxRequest();
        verifyTrue(selenium.isTextPresent("Name: Has less than allowed 3 chars"));
    }



    public void threeTags(String name) {
        
        open("p/".concat(ManageProjectTest.PROJECT_NAME));
        for (int i = 0; i < 3; i++) {
            tags.add("tag "+i);

        }
        ManageConcernTest.createConcern(selenium, "concern-1", name, "desc", tags);
        selenium.click("concernForm:submit");
        waitForAjaxRequest();
        verifyTrue(selenium.isTextPresent("concern-1"));
        verifyTrue(selenium.isTextPresent(name));
        verifyTrue(selenium.isTextPresent("desc"));
        verifyTrue(selenium.isTextPresent("tag 0"));
        verifyTrue(selenium.isTextPresent("tag 1"));
        verifyTrue(selenium.isTextPresent("tag 2"));
        tags.clear();
    }





    private void editConcern(String name) {
        threeTags("alter name");
        selenium.click("//img[@alt='edit']");
        waitForPageToLoad();
        selenium.type("concernForm:inExternalId", "new concernID");
        selenium.type("concernForm:inName", name);
        selenium.type("concernForm:inDescription", "new description");
        selenium.type("//form[@id='concernForm']/div[1]/div[4]/div[2]/input[1]", "test-tag");
        selenium.fireEvent("concernForm:inName", "blur");
        waitForAjaxRequest();
        selenium.click("concernForm:submit");
        waitForAjaxRequest();
        verifyTrue(selenium.isTextPresent("new concernID"));
        verifyTrue(selenium.isTextPresent(name));
        verifyTrue(selenium.isTextPresent("new description"));
        verifyTrue(selenium.isTextPresent("tag 0"));
        verifyTrue(selenium.isTextPresent("test-tag"));
        verifyTrue(selenium.isTextPresent("tag 2"));
    }



    public void testUpdateName() {
        RegistrationTest.registerUserWithDefaulCredentials(selenium);
        LoginTest.loginUserWithDefaulCredentials(selenium);
        addAll();
        ManageProjectTest.createDefaultProject(selenium);
        
        editConcern("erster Concern");
        open("p/".concat(ManageProjectTest.PROJECT_NAME));
        waitForPageToLoad();
        editConcern("zweiter Concern");
        open("p/".concat(ManageProjectTest.PROJECT_NAME));
        waitForPageToLoad();
        verifyFalse(selenium.isTextPresent("alter name"));
        verifyTrue(selenium.isTextPresent("erster Concern"));
        verifyTrue(selenium.isTextPresent("zweiter Concern"));

        selenium.click("//form[@id='concernForm']/table[1]/tbody[1]/tr[1]/td[2]/a[1]");
        waitForPageToLoad();
        verifyTrue(selenium.isTextPresent("zweiter Concern"));
      
    }

}
