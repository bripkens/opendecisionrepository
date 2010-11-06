package nl.rug.search.odr;

import com.thoughtworks.selenium.Selenium;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import org.junit.Test;

/**
 *
 * @author Stefan Arians
 */
public class ManageIterationTest extends AbstractSelenseTestCase {

    private static final String iterationName = "first Iteration";

    private static final String description = "description";

    private static final Date startDate = new GregorianCalendar(2010, 2, 23, 3, 23).getTime();

    private static final Date documentedWhen = new GregorianCalendar(2010, 2, 26, 5, 53).getTime();

    private static final Date endDate = new GregorianCalendar(2010, 3, 3, 12, 34).getTime();




    public static void createDefaultIteration(Selenium selenium, String projectName) {
        createIteration(selenium, projectName, startDate, description, iterationName, documentedWhen, endDate);
    }




    public static void createIteration(Selenium selenium, String projectName, Date startDate, String description, String name,
                                       Date documentedWhen, Date endDate) {

        GregorianCalendar start = new GregorianCalendar();
        start.setTimeInMillis(startDate.getTime());

        GregorianCalendar end = new GregorianCalendar();
        end.setTimeInMillis(endDate.getTime());

        selenium.click("addIterationLink");
        waitForPageToLoad(selenium);
        selenium.type("iterationForm:inName", name);
        selenium.type("iterationForm:inDescription", description);
        selenium.select("iterationForm:_t45:startDate:Month", "label=" + getMonth(start.get(Calendar.MONTH)));
        selenium.select("iterationForm:_t45:startDate:Day", "label=" + getNumberFormat(start.get(Calendar.DAY_OF_MONTH)));
        selenium.select("iterationForm:_t45:startDate:Year", "label=" + start.get(Calendar.YEAR));
        selenium.select("iterationForm:_t45:startDate:Hour", "label=" + start.get(Calendar.HOUR_OF_DAY));
        selenium.select("iterationForm:_t45:startDate:Minute", "label=" + start.get(Calendar.MINUTE));

        selenium.select("iterationForm:_t45:endDate:Month", "label=" + getMonth(end.get(Calendar.MONTH)));
        selenium.select("iterationForm:_t45:endDate:Day", "label=" + getNumberFormat(end.get(Calendar.DAY_OF_MONTH)));
        selenium.select("iterationForm:_t45:endDate:Year", "label=" + end.get(Calendar.YEAR));
        selenium.select("iterationForm:_t45:endDate:Hour", "label=" + end.get(Calendar.HOUR_OF_DAY));
        selenium.select("iterationForm:_t45:endDate:Minute", "label=" + end.get(Calendar.MINUTE));

        selenium.fireEvent("iterationForm:inName", "blur");
        waitForAjaxRequest();
        selenium.click("iterationForm:submit");
        waitForAjaxRequest();

    }




//    @Test
//    public void testcreateIteration() {
//        RegistrationTest.registerUserWithDefaulCredentials(selenium);
//        LoginTest.loginUserWithDefaulCredentials(selenium);
//        addAll();
//
//        ManageProjectTest.createDefaultProject(selenium);
//        open("p/".concat(ManageProjectTest.PROJECT_NAME));
//        ManageIterationTest.createDefaultIteration(selenium, "ODR");
//        waitForAjaxRequest();
//        verifyTrue(selenium.isTextPresent(iterationName));
//    }
//
//
//    @Test
//    public void testDatePickerInIteration() {
//        RegistrationTest.registerUserWithDefaulCredentials(selenium);
//        LoginTest.loginUserWithDefaulCredentials(selenium);
//        addAll();
//        ManageProjectTest.createDefaultProject(selenium);
//        open("p/".concat(ManageProjectTest.PROJECT_NAME));
//        ManageIterationTest.createIteration(selenium, "ODR",
//                new GregorianCalendar(2010, 7, 1, 12, 1).getTime(),
//                "long description", "ODR",
//                new GregorianCalendar(2010, 1, 2, 3, 45).getTime(),
//                new GregorianCalendar(2010, 3, 1, 12, 56).getTime());
//        waitForAjaxRequest();
//        verifyTrue(selenium.isTextPresent("start date after end date"));
//        return;
//
//    }
//
//
//
//
//    @Test
//    public void testWrongName() {
//        RegistrationTest.registerUserWithDefaulCredentials(selenium);
//        LoginTest.loginUserWithDefaulCredentials(selenium);
//        addAll();
//        ManageProjectTest.createDefaultProject(selenium);
//        open("p/".concat(ManageProjectTest.PROJECT_NAME));
//        ManageIterationTest.createIteration(selenium, ManageProjectTest.PROJECT_NAME,
//                new GregorianCalendar(2010, 7, 1, 12, 1).getTime(),
//                "long description", "It",
//                new GregorianCalendar(2010, 1, 2, 3, 45).getTime(),
//                new GregorianCalendar(2010, 3, 1, 12, 56).getTime());
//        waitForAjaxRequest();
//        verifyTrue(selenium.isTextPresent("Has less than allowed 3 chars"));
//        return;
//    }
//
//
//
//
//    public void testUpdateName() {
//        RegistrationTest.registerUserWithDefaulCredentials(selenium);
//        LoginTest.loginUserWithDefaulCredentials(selenium);
//        addAll();
//        ManageProjectTest.createDefaultProject(selenium);
//        open("p/".concat(ManageProjectTest.PROJECT_NAME));
//        ManageIterationTest.createIteration(selenium, ManageProjectTest.PROJECT_NAME,
//                new GregorianCalendar(2010, 2, 2, 12, 1).getTime(),
//                "long description", "Iteration",
//                new GregorianCalendar(2010, 1, 2, 3, 45).getTime(),
//                new GregorianCalendar(2010, 3, 1, 12, 56).getTime());
//        waitForAjaxRequest();
//        waitForPageToLoad();
//        selenium.click("//tr[@id='j_idt61:iterationTable:0']/td[3]/a[2]/img");
//        waitForPageToLoad();
//        String iterationName1 = "iteration2";
//        selenium.type("iterationForm:inName", iterationName1);
//        selenium.fireEvent("iterationForm:inName", "blur");
//        waitForAjaxRequest();
//        selenium.click("iterationForm:submit");
//
//        waitForAjaxRequest();
//        verifyTrue(selenium.isTextPresent(iterationName1));
//    }
//
//
//
//
//    public void testUpdateWrongDate() {
//        RegistrationTest.registerUserWithDefaulCredentials(selenium);
//        LoginTest.loginUserWithDefaulCredentials(selenium);
//        addAll();
//        ManageProjectTest.createDefaultProject(selenium);
//        open("p/".concat(ManageProjectTest.PROJECT_NAME));
//        ManageIterationTest.createIteration(selenium, ManageProjectTest.PROJECT_NAME,
//                new GregorianCalendar(2010, 2, 2, 12, 1).getTime(),
//                "long description", "Iteration",
//                new GregorianCalendar(2010, 1, 2, 3, 45).getTime(),
//                new GregorianCalendar(2010, 3, 10, 12, 56).getTime());
//        waitForAjaxRequest();
//        waitForPageToLoad();
//        selenium.click("//tr[@id='j_idt61:iterationTable:0']/td[3]/a[2]/img");
//        waitForPageToLoad();
//        String iterationName1 = "iteration2";
//        selenium.type("iterationForm:inName", iterationName1);
//        selenium.type("iterationForm:inDescription", "asdfasdf2");
//        selenium.select("iterationForm:_t45:startDate:Month", "label=Dezember");
//        selenium.click("//select[@id='iterationForm:_t45:startDate:Month']/option[12]");
//        selenium.select("iterationForm:_t45:startDate:Day", "label=31");
//        selenium.click("//option[@value='31']");
//        selenium.select("iterationForm:_t45:startDate:Month", "label=Februar");
//        selenium.click("//option[@value='2']");
//        selenium.fireEvent("iterationForm:_t45:startDate:Day", "blur");
//        waitForAjaxRequest();
//        verifyEquals("März", selenium.getSelectedLabel("iterationForm:_t45:startDate:Month"));
//        verifyEquals("03", selenium.getSelectedLabel("iterationForm:_t45:startDate:Day"));
//    }




    private static String getMonth(int month) {
        month -= 1;
        switch (month) {
            case 0:
                return "Januar";
            case 1:
                return "Februar";
            case 2:
                return "März";
            case 3:
                return "April";
            case 4:
                return "Mai";
            case 5:
                return "Juni";
            case 6:
                return "Juli";
            case 7:
                return "August";
            case 8:
                return "September";
            case 9:
                return "Oktober";
            case 10:
                return "November";
            case 11:
                return "Dezember";
            default:
                return "null";

        }

    }




    private static String getNumberFormat(int number) {
        if (number < 10) {
            return 0 + "" + number;
        } else {
            return number + "";
        }
    }
}
