package nl.rug.search.odr.entities;

import nl.rug.search.odr.BusinessException;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
public class OprLinkTest {

    private OprLink l;




    @Before
    public void setUp() {
        l = new OprLink();
    }




    @Test
    public void testInitialization() {
        assertNull(l.getId());
        assertNull(l.getLink());
    }




    @Test
    public void testId() {
        l.setId(Long.MIN_VALUE);

        assertEquals(Long.MIN_VALUE, (long) l.getId());
    }




    @Test(expected = BusinessException.class)
    public void testIdNull() {
        l.setId(null);
    }




    @Test
    public void testComparedata() {
        assertEquals(1, l.getCompareData().length);

        assertNull(l.getCompareData()[0]);

        l.setLink("Foo");
        assertEquals("Foo", l.getCompareData()[0]);
    }




    @Test
    public void testSetLink() {
        l.setLink("12345");

        assertEquals("12345", l.getLink());
    }




    @Test(expected = BusinessException.class)
    public void testLinkNull() {
        l.setLink(null);
    }



    @Test
    public void testIsPersistable() {
        assertFalse(l.isPersistable());

        l.setLink("12345");

        assertTrue(l.isPersistable());
    }
}
