package nl.rug.search.odr.entities;

import nl.rug.search.odr.BusinessException;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Ben
 */
public class StateTest {

    private State s;




    @Before
    public void setUp() {
        s = new State();
        s.setCommon(false);
    }




    @Test
    public void testInitialization() {
        assertNull(s.getId());
        assertNull(s.getStatusName());
        assertNull(s.getActionName());
        assertFalse(s.isCommon());
        assertFalse(s.isInitialState());
    }




    @Test
    public void testId() {
        Long id = 1L;
        s.setId(id);
        assertEquals(id, s.getId());
    }




    @Test(expected = BusinessException.class)
    public void testNullId() {
        s.setId(null);
    }




    @Test
    public void testIsCommon() {
        s.setCommon(true);
        assertTrue(s.isCommon());

        s.setCommon(false);
        assertFalse(s.isCommon());
    }




    @Test
    public void testIsInitialState() {
        s.setInitialState(true);
        assertTrue(s.isInitialState());

        s.setInitialState(false);
        assertFalse(s.isInitialState());
    }




    @Test
    public void testSetStatusName() {
        String name = "Foo";
        s.setStatusName(name);

        assertEquals(name, s.getStatusName());
    }




    @Test(expected = BusinessException.class)
    public void testSetStatusNameNull() {
        s.setStatusName(null);
    }




    @Test(expected = BusinessException.class)
    public void testSetStatusNameEmpty() {
        s.setStatusName("   ");
    }




    @Test
    public void testSetActionName() {
        String name = "Foo";
        s.setActionName(name);

        assertEquals(name, s.getActionName());
    }




    @Test(expected = BusinessException.class)
    public void testSetActionNameNull() {
        s.setActionName(null);
    }




    @Test(expected = BusinessException.class)
    public void testSetActionNameEmpty() {
        s.setActionName("   ");
    }




    @Test
    public void testIsPersistable() {
        assertFalse(s.isPersistable());

        s.setActionName("abcd");

        assertFalse(s.isPersistable());

        s.setStatusName("dcda");

        assertTrue(s.isPersistable());
    }




    @Test
    public void testIsPersistableOtherway() {
        assertFalse(s.isPersistable());

        s.setStatusName("dcda");

        assertFalse(s.isPersistable());

        s.setActionName("abcd");

        assertTrue(s.isPersistable());
    }




    @Test
    public void testGetCompareData() {
        String actionName = "Bla";
        String statusName = "Foo";
        boolean initialState = false;
        boolean common = true;

        s.setActionName(actionName);
        s.setStatusName(statusName);
        s.setCommon(common);
        s.setInitialState(initialState);

        assertEquals(statusName, s.getCompareData()[0]);
        assertEquals(actionName, s.getCompareData()[1]);
        assertEquals(common, s.getCompareData()[2]);
        assertEquals(initialState, s.getCompareData()[3]);
    }
}
