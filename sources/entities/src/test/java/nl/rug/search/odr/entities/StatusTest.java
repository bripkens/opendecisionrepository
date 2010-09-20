package nl.rug.search.odr.entities;

import nl.rug.search.odr.BusinessException;
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
        assertNull(s.getStatusId());
        assertNull(s.getName());
    }

    @Test
    public void testId(){
        Long id = 1L;
        s.setStatusId(id);
        assertEquals(id, s.getStatusId());
    }

   @Test(expected = BusinessException.class)
    public void testNullId() {
       s.setStatusId(null);
    }

    @Test
    public void testSetName() {
        s.setName("foo");
        assertEquals("foo", s.getName());
    }

    @Test(expected = BusinessException.class)
    public void testNullName() {
       s.setName(null);
    }

    @Test(expected = BusinessException.class)
    public void testEmptyName() {
        s.setName("        ");
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
