package nl.rug.search.odr.entities;

import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Stefan
 */
public class StatusTest {

    private Status s;

    @Before
    public void setUp() {
        s = new Status();
        s.setCommon(false);
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testInitialization() {
        assertNull(s.getId());
        assertNull(s.getName());
    }

    @Test
    public void testSetName() {
        s.setName("foo");
        assertEquals("foo", s.getName());
    }

    @Test
    public void testIsCommon() {
        s.setCommon(true);
        assertEquals(true, s.isCommon());
    }

    @Test
    public void testIsNotCommon() {
        s.setCommon(false);
        assertEquals(false, s.isCommon());
    }
}
