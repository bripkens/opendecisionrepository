package nl.rug.search.odr.entities;

import java.util.Date;
import java.util.GregorianCalendar;
import nl.rug.search.odr.BusinessException;
import nl.rug.search.odr.TestUtil;
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
        assertNull(i.getId());
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

    @Test
    public void testSetIterationId() {
        long id = Long.MIN_VALUE;
        i.setId(id);
        assertEquals(id, (long) i.getId());
    }

    @Test(expected = BusinessException.class)
    public void testSetNullIterationId() {
        i.setId(null);
    }

    @Test
    public void testSetName() {
        String name = "foo";
        i.setName(name);
        assertEquals(name, i.getName());
    }

    @Test(expected = BusinessException.class)
    public void testNullName() {
        i.setName(null);
    }

    @Test(expected = BusinessException.class)
    public void testEmptyName() {
        i.setName("  ");
    }

    @Test
    public void testSetStartdate() {
        Date startDate = new Date();
        i.setStartDate(startDate);
        assertEquals(startDate, i.getStartDate());
    }

    @Test
    public void testStartDate() {
        Date startDate = new Date();
        //endDate is not empty and before startdate
        long millis = new Date().getTime() + 10000;

        Date endDate = new Date();
        endDate.setTime(millis);

        i.setStartDate(startDate);
        i.setEndDate(endDate);
    }

    @Test(expected = BusinessException.class)
    public void testWrongStartDate() {
        Date startDate = new Date();
        //endDate is not empty and before startdate
        long millis = new Date().getTime() + 10000;

        Date endDate = new Date();
        endDate.setTime(millis);

        i.setEndDate(startDate);
        i.setStartDate(endDate);


    }

    @Test(expected = BusinessException.class)
    public void testNullStartDate() {
        i.setStartDate(null);
    }

    //endDate is before startDate
    @Test(expected = BusinessException.class)
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

    @Test(expected = BusinessException.class)
    public void testNullEndDate() {
        i.setEndDate(null);
    }

    @Test
    public void testHashCode() {
        Iteration i2 = new Iteration();
        i2.setId(Long.MIN_VALUE);
        i2.setDescription("bla");
        i2.setName("bla");

        Date startdate = new Date();
        i2.setStartDate(startdate);
        i2.setEndDate(new Date(startdate.getTime() + 1000));

        Iteration i3 = new Iteration();
        i3.setId(Long.MIN_VALUE);
        i3.setDescription("bla");
        i3.setName("bla");

        Date startdate1 = new Date();
        i3.setStartDate(startdate1);
        i3.setEndDate(new Date(startdate1.getTime() + 1000));

        assertEquals(i2.hashCode(), i3.hashCode());
        TestUtil.assertNotEquals(i.hashCode(), i2.hashCode());
    }

    @Test
    public void testEquals() {
        assertFalse(i.equals(new TestUtil()));

        Iteration i2 = new Iteration();
        i2.setDescription("bla");
        i2.setName("bla");

        Date startdate = new Date();
        i2.setStartDate(startdate);
        i2.setEndDate(new Date(startdate.getTime() + 1000));

        Iteration i3 = new Iteration();
        i3.setDescription("foo");
        i3.setName("foo");

        Date startdate1 = new Date();
        i3.setStartDate(startdate1);
        i3.setEndDate(new Date(startdate1.getTime() + 1000));

        assertFalse(i2.equals(i3));

        assertTrue(i.equals(i));
    }

    @Test
    public void testNullEquals() {
        assertFalse(i.equals(null));
    }

    @Test
    public void testDuration() {
        GregorianCalendar start = new GregorianCalendar();
        start.set(2010, 10, 4);

        GregorianCalendar end = new GregorianCalendar();
        end.set(2010, 10, 9);

        i.setStartDate(start.getTime());
        i.setEndDate(end.getTime());

        assertEquals(5, i.getDurationInDays());
    }

    @Test
    public void testEndDateComparatorBothNull() {
        Iteration i2 = new Iteration();
        Iteration.EndDateComparator comp = new Iteration.EndDateComparator();
        assertEquals(0, comp.compare(i, i2));
    }

    @Test
    public void testEndDateComparatorFirstNull() {
        Iteration i2 = new Iteration();
        i2.setEndDate(new Date());
        Iteration.EndDateComparator comp = new Iteration.EndDateComparator();
        assertEquals(-1, comp.compare(i, i2));
    }

    @Test
    public void testEndDateComparatorSecondNull() {
        i.setEndDate(new Date());
        Iteration i2 = new Iteration();
        Iteration.EndDateComparator comp = new Iteration.EndDateComparator();
        assertEquals(1, comp.compare(i, i2));
    }

    @Test
    public void testEndDateComparatorFirstSmaller() {
        i.setEndDate(new Date());
        Iteration i2 = new Iteration();
        i2.setEndDate(new Date(i.getEndDate().getTime() + 1000));
        Iteration.EndDateComparator comp = new Iteration.EndDateComparator();
        assertEquals(-1, comp.compare(i, i2));
    }

    @Test
    public void testEndDateComparatorBothEven() {
        Date date = new Date();
        i.setEndDate(date);
        Iteration i2 = new Iteration();
        i2.setEndDate(date);
        Iteration.EndDateComparator comp = new Iteration.EndDateComparator();
        assertEquals(0, comp.compare(i, i2));
    }

    @Test
    public void testEndDateComparatorSecondSmaller() {
        Date date = new Date();
        i.setEndDate(date);
        Iteration i2 = new Iteration();
        i2.setEndDate(date);
        Iteration.EndDateComparator comp = new Iteration.EndDateComparator();
        assertEquals(0, comp.compare(i2, i));
    }
}