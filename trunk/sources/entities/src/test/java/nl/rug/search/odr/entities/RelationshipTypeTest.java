package nl.rug.search.odr.entities;

import nl.rug.search.odr.BusinessException;
import nl.rug.search.odr.TestUtil;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Stefan
 */
public class RelationshipTypeTest {

    private RelationshipType r;




    @Before
    public void setUp() {
        r = new RelationshipType();
    }




    @Test
    public void testInitialization() {
        assertNull(r.getId());
        assertNull(r.getName());
        assertFalse(r.isCommon());
    }




    @Test
    public void testId() {
        Long id = 1L;
        r.setId(id);
        assertEquals(id, r.getId());
    }




    @Test(expected = BusinessException.class)
    public void testNullId() {
        r.setId(null);
    }




    @Test
    public void testSetName() {
        r.setName("foo");
        assertEquals("foo", r.getName());
    }




    @Test(expected = BusinessException.class)
    public void testNullName() {
        r.setName(null);
    }




    @Test(expected = BusinessException.class)
    public void testEmptyName() {
        r.setName("        ");
    }




    @Test
    public void testIsCommon() {
        r.setCommon(true);
        assertEquals(true, r.isCommon());
    }




    @Test
    public void testIsNotCommon() {
        r.setCommon(false);
        assertEquals(false, r.isCommon());
    }




    @Test
    public void testHashCode() {
        RelationshipType r2 = new RelationshipType();
        r2.setId(Long.MIN_VALUE);
        r2.setCommon(true);
        r2.setName("foo");

        RelationshipType r3 = new RelationshipType();
        r3.setId(Long.MIN_VALUE);
        r3.setCommon(true);
        r3.setName("foo");


        assertEquals(r2.hashCode(), r3.hashCode());
        TestUtil.assertNotEquals(r.hashCode(), r2.hashCode());
    }




    @Test
    public void testEquals() {
        assertFalse(r.equals(new TestUtil()));

        RelationshipType r2 = new RelationshipType();
        r2.setId(Long.MIN_VALUE);
        r2.setCommon(true);
        r2.setName("foo");

        RelationshipType r3 = new RelationshipType();
        r3.setId(Long.MIN_VALUE);
        r3.setCommon(true);
        r3.setName("bla");

        assertTrue(r2.equals(r3));

        assertTrue(r.equals(r));
    }


    @Test
    public void testIsPersistable() {
        assertFalse(r.isPersistable());

        r.setName("abcd");

        assertTrue(r.isPersistable());
    }

    @Test
    public void testGetCompareData() {
        String name = "klad";
        boolean common = true;

        r.setName(name);
        r.setCommon(common);

        assertEquals(name, r.getCompareData()[0]);
        assertEquals(common, r.getCompareData()[1]);
    }
}
