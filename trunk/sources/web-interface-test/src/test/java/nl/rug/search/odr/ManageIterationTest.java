package nl.rug.search.odr;

import com.sun.org.apache.bcel.internal.generic.CALOAD;
import com.thoughtworks.selenium.Selenium;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import org.junit.Ignore;
import org.junit.Test;

/**
 *
 * @author Stefan Arians
 */
@Ignore
public class ManageIterationTest extends AbstractSelenseTestCase {

    private static final String iterationName = "First Iteration";

    private static final String description = "simple description";

    private static final Date startDate = new GregorianCalendar(2010, 10, 20).getTime();

    private static final Date documentedWhen = new GregorianCalendar(2010, 10, 22).getTime();




    ;
    private static final Date endDate = new GregorianCalendar(2010, 10, 25).getTime();




    public static void createDefaultIteration(Selenium selenium, String projectName) {
        createIteration(selenium, projectName, new GregorianCalendar(2010, 2, 23, 3, 23).getTime(), "description", "first Iteration", new GregorianCalendar(2010, 2, 26, 5, 53).
                getTime(), new GregorianCalendar(2010, 3, 3, 12, 34).getTime());
    }




    public static void createIteration(Selenium selenium, String projectName, Date startDate, String description, String name,
                                       Date documentedWhen, Date endDate) {

        GregorianCalendar start = new GregorianCalendar();
        start.setTimeInMillis(startDate.getTime());

        GregorianCalendar end = new GregorianCalendar();
        end.setTimeInMillis(endDate.getTime());

        open(selenium, "p/" + projectName);
        selenium.click("addIterationLink");
       waitForPageToLoad(selenium);
        selenium.type("iterationForm:inName", name);
        selenium.type("iterationForm:inDescription", description);
        selenium.type("iterationForm:_t46:startDate:date", start.get(Calendar.YEAR) + "-" + start.get(Calendar.MONTH) + "-" + start.
                get(Calendar.DAY_OF_MONTH));
        selenium.type("iterationForm:_t46:endDate:date", end.get(Calendar.YEAR) + "-" + end.get(Calendar.MONTH) + "-" + end.
                get(Calendar.DAY_OF_MONTH));
        waitForAjaxRequest();
        sleep(AJAX_SLEEP_MILLIS);
        selenium.click("iterationForm:submit");
        sleep(AJAX_SLEEP_MILLIS);
    }

    @Test
    public void testcreateIteration(){
        RegistrationTest.registerUserWithDefaulCredentials(selenium);

        LoginTest.loginUserWithDefaulCredentials(selenium);

        addStakeholderRoles();

        ManageProjectTest.createDefaultProject(selenium);
        LoginTest.loginUserWithDefaulCredentials(selenium);

        open("p/ODR");

        ManageIterationTest.createDefaultIteration(selenium, "ODR");
        sleep(30000);
    }
}
