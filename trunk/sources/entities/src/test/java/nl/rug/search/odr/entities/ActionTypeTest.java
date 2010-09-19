package nl.rug.search.odr.entities;

import java.util.ArrayList;
import java.util.Collection;
import nl.rug.search.odr.TestUtil;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Stefan
 */
public class ActionTypeTest {

    private ActionType a;

    @Before
    public void setUp() {
        a = new ActionType();
    }

    @Test
    public void testInitialization() {
        assertNull(a.getId());
        assertNull(a.getName());
    }

    @Test
    public void testId() {
        long id = 1;
        a.setId(id);
        assertEquals(1, id);
    }

    @Test
    public void testSetName() {
        String name = "foo";
        a.setName(name);
        assertEquals(name, a.getName());
    }

    @Test(expected = NullPointerException.class)
    public void testNullName() {
        a.setName(null);
    }

    @Test(expected = RuntimeException.class)
    public void testEmptyName() {
        a.setName("  ");
    }

    @Test(expected = NullPointerException.class)
    public void testNullId() {
        a.setId(null);
    }

    @Test
    public void testSetCommonTrue() {
        a.setCommon(true);
        assertTrue(a.isCommon());
    }

    @Test
    public void testSetCommonFalse() {
        a.setCommon(false);
        assertFalse(a.isCommon());
    }

    @Test
    public void setToString() {
        assertTrue(TestUtil.toStringHelper(a));
    }

    @Test
    public void testHashCode(){
        ActionType at = new ActionType();
        at.setId(Long.MIN_VALUE);
        at.setCommon(true);
        at.setName("bla");

        ActionType at1 = new ActionType();
        at1.setId(Long.MIN_VALUE);
        at1.setCommon(true);
        at1.setName("bla");

        assertEquals(at.hashCode(),at1.hashCode());
        TestUtil.assertNotEquals(a.hashCode(), at.hashCode());
    }
}
