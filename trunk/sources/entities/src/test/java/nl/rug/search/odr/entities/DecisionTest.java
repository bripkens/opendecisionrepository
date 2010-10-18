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

    private Decision a;

    @Before
    public void setUp() {
        a = new Decision();
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testInitialization() {
        assertNull(a.getId());
        assertNull(a.getName());
    }

    @Test
    public void testSetId() {
        a.setId(Long.MIN_VALUE);
        assertEquals(Long.MIN_VALUE, (long) a.getId());
    }

    @Test(expected = BusinessException.class)
    public void testSetNullId() {
        a.setId(null);
    }

    @Test
    public void testSetName() {
        a.setName("foo");
        assertEquals("foo", a.getName());
    }

    @Test(expected = BusinessException.class)
    public void testNullName() {
       a.setName(null);
    }

    @Test(expected = BusinessException.class)
    public void testEmptyName() {
        a.setName("        ");
    }

//    @Test
//    public void testaddVerison(){
//        Version v = new Version();
//        Version v2 = new Version();
//        assertTrue(TestUtil.containsReference(a.getVersions(),v));
//        assertFalse(TestUtil.containsReference(a.getVersions(),v2));
//    }
    
//    @Test (expected = BusinessException.class)
//    public void testNullAddVerison(){
//        a.addVerison(null);
//
//    }

    @Test
    public void testHashCode(){
        Decision at = new Decision();
        at.setId(Long.MIN_VALUE);
        at.setName("bla");

        Decision at1 = new Decision();
        at1.setId(Long.MIN_VALUE);
        at1.setName("bla");

        assertEquals(at.hashCode(),at1.hashCode());
        TestUtil.assertNotEquals(a.hashCode(), at.hashCode());
    }

    @Test
    public void testEquals(){
        assertFalse(a.equals(new TestUtil()));

        Decision at1 = new Decision();
        at1.setName("bla");

        Decision at2 = new Decision();
        at2.setName("foo");

        assertFalse(at1.equals(at2));
        
        assertTrue(a.equals(a));
    }

    @Test
    public void testNullEquals(){
        assertFalse(a.equals(null));
    }
    
}
