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
        selenium.type("iterationForm:_t46:startDate:date", start.get(Calendar.YEAR) + "-" + start.get(Calendar.MONTH) + "-" + start.
                get(Calendar.DAY_OF_MONTH));
        selenium.type("iterationForm:_t46:endDate:date", end.get(Calendar.YEAR) + "-" + end.get(Calendar.MONTH) + "-" + end.
                get(Calendar.DAY_OF_MONTH));
        selenium.fireEvent("iterationForm:inName", "blur");
        waitForAjaxRequest();
        selenium.click("iterationForm:submit");

    }




    @Test
    public void testcreateIteration() {
        RegistrationTest.registerUserWithDefaulCredentials(selenium);
        LoginTest.loginUserWithDefaulCredentials(selenium);
        addStakeholderRoles();
        ManageProjectTest.createDefaultProject(selenium);
        open("p/ODR");
        ManageIterationTest.createDefaultIteration(selenium, "ODR");
        verifyTrue(selenium.isTextPresent("first Iteration"));
    }




    @Test
    public void testDatePickerInIteration() {
        RegistrationTest.registerUserWithDefaulCredentials(selenium);
        LoginTest.loginUserWithDefaulCredentials(selenium);
        addStakeholderRoles();
        ManageProjectTest.createDefaultProject(selenium);
        open("p/".concat(ManageProjectTest.PROJECT_NAME));
        ManageIterationTest.createIteration(selenium, "ODR",
                new GregorianCalendar(2010, 1, 1, 12, 1).getTime(),
                "long description", "ODR",
                new GregorianCalendar(2010, 1, 2, 12, 1).getTime(),
                new GregorianCalendar(2010, 0, 1, 12, 1).getTime());
        verifyTrue(selenium.isTextPresent("start date is after end date"));

    }
}
