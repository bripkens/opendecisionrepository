package nl.rug.search.odr.entities;

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
public class DecisionTest {

    private Decision d;




    @Before
    public void setUp() {
        d = new Decision();
    }




    @After
    public void tearDown() {
    }




    @Test
    public void testInitialization() {
        assertNull(d.getId());
        assertNull(d.getName());
    }




    @Test
    public void testSetId() {
        d.setId(Long.MIN_VALUE);
        assertEquals(Long.MIN_VALUE, (long) d.getId());
    }




    @Test(expected = BusinessException.class)
    public void testSetNullId() {
        d.setId(null);
    }




    @Test
    public void testSetName() {
        d.setName("foo");
        assertEquals("foo", d.getName());
    }




    @Test(expected = BusinessException.class)
    public void testNullName() {
        d.setName(null);
    }




    @Test(expected = BusinessException.class)
    public void testEmptyName() {
        d.setName("        ");
    }



    @Test(expected=BusinessException.class)
    public void testNameTooShort() {
        d.setName("ab");
    }
    
    
    @Test(expected=BusinessException.class)
    public void testNameTooLong() {
        d.setName("123456789012345678901234567890123456789012345678901");
    }


    @Test
    public void testIsPersistable() {
        assertFalse(d.isPersistable());

        d.setName("abcd");

        assertFalse(d.isPersistable());

        d.setTemplate(new DecisionTemplate());

        assertFalse(d.isPersistable());

        d.addVersion(new Version());

        assertTrue(d.isPersistable());
    }


    @Test
    public void testHashCode() {
        Decision at = new Decision();
        at.setId(Long.MIN_VALUE);
        at.setName("bla");

        Decision at1 = new Decision();
        at1.setId(Long.MIN_VALUE);
        at1.setName("bla");

        assertEquals(at.hashCode(), at1.hashCode());
        TestUtil.assertNotEquals(d.hashCode(), at.hashCode());
    }




    @Test
    public void testEquals() {
        assertFalse(d.equals(new TestUtil()));

        Decision at1 = new Decision();
        at1.setName("bla");

        Decision at2 = new Decision();
        at2.setName("foo");

        assertFalse(at1.equals(at2));

        assertTrue(d.equals(d));
    }




    @Test
    public void testNullEquals() {
        assertFalse(d.equals(null));
    }
}
