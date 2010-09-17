package nl.rug.search.odr.entities;

import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Stefan
 */
public class RelationshipTypeTest {

    private Relationshiptype r;

    @Before
    public void setUp() {
        r = new Relationshiptype();
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testInitialization() {
        assertNull(r.getId());
        assertNull(r.getName());
        r.setCommon(false);
    }


    //ID TEST/
    @Test
    public void testId(){
        Long id = 1L;
        r.setId(id);
        assertEquals(id, r.getId());
    }

   @Test(expected = NullPointerException.class)
    public void testNullId() {
       r.setId(null);
    }

   //NAME TEST/
    @Test
    public void testSetName() {
        r.setName("foo");
        assertEquals("foo", r.getName());
    }

    @Test(expected = NullPointerException.class)
    public void testNullName() {
       r.setName(null);
    }

    @Test(expected = RuntimeException.class)
    public void testEmptyName() {
        r.setName("        ");
    }

    //COMMON TEST/
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
}
