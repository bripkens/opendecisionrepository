package nl.rug.search.odr.entities;

import java.util.Date;
import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Stefan
 */
public class IterationTest {

    private Iteration i;

    @Before
    public void setUp() {
        i = new Iteration();
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testInitialization() {
        assertNull(i.getDescription());
        assertNull(i.getIterationId());
        assertNull(i.getName());
        assertNull(i.getStartDate());
        assertNull(i.getEndDate());
    }

    @Test
    public void testSetDescription() {
        String foo = "fooo";
        i.setDescription(foo);
        assertEquals(foo, i.getDescription());
    }

    @Test(expected = NullPointerException.class)
    public void testSetNullDescription() {
        i.setDescription(null);
    }

    @Test(expected = RuntimeException.class)
    public void testSetEmptyDescription() {
        i.setDescription("          ");
    }

    @Test
    public void testSetIterationId() {
        long id = Long.MIN_VALUE;
        i.setId(id);
        assertEquals(id, (long) i.getIterationId());
    }

    @Test(expected = NullPointerException.class)
    public void testSetNullIterationId() {
        i.setId(null);
    }

    @Test
    public void testSetName() {
        String name = "foo";
        i.setName(name);
        assertEquals(name, i.getName());
    }

    @Test(expected = NullPointerException.class)
    public void testNullName() {
        i.setName(null);
    }

    @Test(expected = RuntimeException.class)
    public void testEmptyName() {
        i.setName("  ");
    }

    @Test(expected = RuntimeException.class)
    public void testSetStartdate() {
        Date startDate = new Date();
        i.setStartDate(startDate);
        assertEquals(startDate, i.getStartDate());

        //endDate is not empty and before startdate
        long millis = new Date().getTime() + 10000;

        Date endDate = new Date();
        endDate.setTime(millis);

        i.setStartDate(endDate);
        i.setEndDate(startDate);
    }

    @Test(expected = NullPointerException.class)
    public void testNullStartDate() {
        i.setStartDate(null);
    }

    //endDate is before startDate
    @Test(expected = RuntimeException.class)
    public void testSetEnddate() {
        long millis = new Date().getTime() - 10000;

        Date endDate = new Date();
        endDate.setTime(millis);

        Date startDate = new Date();
        i.setEndDate(endDate);

        assertEquals(endDate, i.getEndDate());

        i.setStartDate(startDate);
        i.setEndDate(endDate);

    }

    @Test(expected = NullPointerException.class)
    public void testNullEndDate() {
        i.setEndDate(null);
    }

    @Test
    public void testSetVersion() {
        Version v = new Version();
        i.setVersion(v);

        assertEquals(v, i.getVersion());
    }

    @Test(expected = NullPointerException.class)
    public void testNullVersion() {
        i.setVersion(null);
    }
}
