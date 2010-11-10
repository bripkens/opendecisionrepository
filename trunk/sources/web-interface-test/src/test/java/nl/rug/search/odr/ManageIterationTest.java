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
    private static final String FORM_SECTION_REMOVER = "1";
    private static final String START_DATE = "3";
    private static final String DATE_TIME_PICKER = "1";
    private static final String END_DATE = "4";
    //START DATE IDS
    public static final String START_MONTH_ID = "//form[@id='iterationForm']/div[" + FORM_SECTION_REMOVER + "]/div"
                                                + "[" + START_DATE + "]/div[" + DATE_TIME_PICKER + "]/select[2]";
    public static final String START_DAY_ID = "//form[@id='iterationForm']/div[" + FORM_SECTION_REMOVER + "]/div"
                                              + "[" + START_DATE + "]/div[" + DATE_TIME_PICKER + "]/select[1]";
    public static final String START_YEAR_ID = "//form[@id='iterationForm']/div[" + FORM_SECTION_REMOVER + "]/div"
                                               + "[" + START_DATE + "]/div[" + DATE_TIME_PICKER + "]/select[3]";
    public static final String START_HOUR_ID = "//form[@id='iterationForm']/div[" + FORM_SECTION_REMOVER + "]/div"
                                               + "[" + START_DATE + "]/div[" + DATE_TIME_PICKER + "]/select[4]";
    public static final String START_MINUTE_ID = "//form[@id='iterationForm']/div[" + FORM_SECTION_REMOVER + "]/div"
                                                 + "[" + START_DATE + "]/div[" + DATE_TIME_PICKER + "]/select[5]";
    //START DATE IDS
    public static final String END_MONTH_ID = "//form[@id='iterationForm']/div[" + FORM_SECTION_REMOVER + "]/div"
                                              + "[" + END_DATE + "]/div[" + DATE_TIME_PICKER + "]/select[2]";
    public static final String END_DAY_ID = "//form[@id='iterationForm']/div[" + FORM_SECTION_REMOVER + "]/div"
                                            + "[" + END_DATE + "]/div[" + DATE_TIME_PICKER + "]/select[1]";
    public static final String END_YEAR_ID = "//form[@id='iterationForm']/div[" + FORM_SECTION_REMOVER + "]/div"
                                             + "[" + END_DATE + "]/div[" + DATE_TIME_PICKER + "]/select[3]";
    public static final String END_HOUR_ID = "//form[@id='iterationForm']/div[" + FORM_SECTION_REMOVER + "]/div"
                                             + "[" + END_DATE + "]/div[" + DATE_TIME_PICKER + "]/select[4]";
    public static final String END_MINUTE_ID = "//form[@id='iterationForm']/div[" + FORM_SECTION_REMOVER + "]/div"
                                               + "[" + END_DATE + "]/div[" + DATE_TIME_PICKER + "]/select[5]";




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

        //INFORMATIONS
        selenium.type("iterationForm:inName", name);
        selenium.type("iterationForm:inDescription", description);

        //STARTDATE
        selenium.select(START_MONTH_ID, "label=" + getMonth(start.get(Calendar.MONTH)));
        selenium.select(START_DAY_ID, "label=" + getNumberFormat(start.get(Calendar.DAY_OF_MONTH)));
        selenium.select(START_YEAR_ID, "label=" + start.get(Calendar.YEAR));
        selenium.select(START_HOUR_ID, "label=" + start.get(Calendar.HOUR_OF_DAY));
        selenium.select(START_MINUTE_ID, "label=" + start.get(Calendar.MINUTE));

        //ENDDATE
        selenium.select(END_MONTH_ID, "label=" + getMonth(end.get(Calendar.MONTH)));
        selenium.select(END_DAY_ID, "label=" + getNumberFormat(end.get(Calendar.DAY_OF_MONTH)));
        selenium.select(END_YEAR_ID, "label=" + end.get(Calendar.YEAR));
        selenium.select(END_HOUR_ID, "label=" + end.get(Calendar.HOUR_OF_DAY));
        selenium.select(END_MINUTE_ID, "label=" + end.get(Calendar.MINUTE));

        //SUBMIT
        selenium.fireEvent("iterationForm:inName", "blur");
        waitForAjaxRequest();
        selenium.click("iterationForm:submit");
        waitForAjaxRequest();

    }




    @Test
    public void testcreateIteration() {
        RegistrationTest.registerUserWithDefaulCredentials(selenium);
        LoginTest.loginUserWithDefaulCredentials(selenium);
        addAll();

        ManageProjectTest.createDefaultProject(selenium);
        open("p/".concat(ManageProjectTest.PROJECT_NAME));
        ManageIterationTest.createDefaultIteration(selenium, "ODR");
        waitForAjaxRequest();
        verifyTrue(selenium.isTextPresent(iterationName));
    }




    @Test
    public void testDatePickerInIteration() {
        RegistrationTest.registerUserWithDefaulCredentials(selenium);
        LoginTest.loginUserWithDefaulCredentials(selenium);
        addAll();
        ManageProjectTest.createDefaultProject(selenium);
        open("p/".concat(ManageProjectTest.PROJECT_NAME));
        ManageIterationTest.createIteration(selenium, "ODR",
                new GregorianCalendar(2010, 7, 1, 12, 1).getTime(),
                "long description", "ODR",
                new GregorianCalendar(2010, 1, 2, 3, 45).getTime(),
                new GregorianCalendar(2010, 3, 1, 12, 56).getTime());
        waitForAjaxRequest();
        verifyTrue(selenium.isTextPresent("start date after end date"));
        return;

    }




    @Test
    public void testWrongName() {
        RegistrationTest.registerUserWithDefaulCredentials(selenium);
        LoginTest.loginUserWithDefaulCredentials(selenium);
        addAll();
        ManageProjectTest.createDefaultProject(selenium);
        open("p/".concat(ManageProjectTest.PROJECT_NAME));
        ManageIterationTest.createIteration(selenium, ManageProjectTest.PROJECT_NAME,
                new GregorianCalendar(2010, 7, 1, 12, 1).getTime(),
                "long description", "It",
                new GregorianCalendar(2010, 1, 2, 3, 45).getTime(),
                new GregorianCalendar(2010, 3, 1, 12, 56).getTime());
        waitForAjaxRequest();
        verifyTrue(selenium.isTextPresent("Has less than allowed 3 chars"));
        return;
    }




    public void testUpdateName() {
        RegistrationTest.registerUserWithDefaulCredentials(selenium);
        LoginTest.loginUserWithDefaulCredentials(selenium);
        addAll();
        ManageProjectTest.createDefaultProject(selenium);
        open("p/".concat(ManageProjectTest.PROJECT_NAME));
        ManageIterationTest.createIteration(selenium, ManageProjectTest.PROJECT_NAME,
                new GregorianCalendar(2010, 2, 2, 12, 1).getTime(),
                "long description", "Iteration",
                new GregorianCalendar(2010, 1, 2, 3, 45).getTime(),
                new GregorianCalendar(2010, 3, 1, 12, 56).getTime());
        waitForAjaxRequest();
        waitForPageToLoad();
        selenium.click("iterationForm:reset");
        waitForPageToLoad();
        selenium.click("//form[@id='iterationForm']/table/tbody/tr[1]/td[3]/a[2]/img");
        waitForPageToLoad();
        String iterationName1 = "iteration2";
        selenium.type("iterationForm:inName", iterationName1);
        selenium.fireEvent("iterationForm:inName", "blur");
        waitForAjaxRequest();
        selenium.click("iterationForm:submit");

        waitForAjaxRequest();
        verifyTrue(selenium.isTextPresent(iterationName1));
    }

//
//
//



    public void testUpdateWrongDate() {
        RegistrationTest.registerUserWithDefaulCredentials(selenium);
        LoginTest.loginUserWithDefaulCredentials(selenium);
        addAll();
        ManageProjectTest.createDefaultProject(selenium);
        open("p/".concat(ManageProjectTest.PROJECT_NAME));
        ManageIterationTest.createIteration(selenium, ManageProjectTest.PROJECT_NAME,
                new GregorianCalendar(2010, 2, 2, 12, 1).getTime(),
                "long description", "Iteration",
                new GregorianCalendar(2010, 1, 2, 3, 45).getTime(),
                new GregorianCalendar(2010, 3, 10, 12, 56).getTime());
        waitForAjaxRequest();
        waitForPageToLoad();
        selenium.click("iterationForm:reset");
        waitForPageToLoad();
        selenium.click("//form[@id='iterationForm']/table/tbody/tr[1]/td[3]/a[2]/img");
        waitForPageToLoad();
        String iterationName1 = "iteration2";
        selenium.type("iterationForm:inName", iterationName1);
        selenium.type("iterationForm:inDescription", "asdfasdf2");
        selenium.select(START_MONTH_ID, "label=December");
        waitForAjaxRequest();
        selenium.select(START_DAY_ID, "label=31");
        selenium.select(START_MONTH_ID, "label=February");
        selenium.fireEvent(START_DAY_ID, "blur");
        waitForAjaxRequest();
        verifyEquals("March", selenium.getSelectedLabel(START_MONTH_ID));
        verifyEquals("03", selenium.getSelectedLabel(START_DAY_ID));
    }




    private static String getMonth(int month) {
        month -= 1;
        switch (month) {
            case 0:
                return "January";
            case 1:
                return "February";
            case 2:
                return "March";
            case 3:
                return "April";
            case 4:
                return "May";
            case 5:
                return "June";
            case 6:
                return "July";
            case 7:
                return "August";
            case 8:
                return "September";
            case 9:
                return "October";
            case 10:
                return "November";
            case 11:
                return "December";
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
