package nl.rug.search.odr.entities;

import nl.rug.search.odr.TestUtil;
import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Stefan
 */
public class ActionTest {

    private Action a;

    @Before
    public void setUp() {
        a = new Action();
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testInitialization() {
        assertNull(a.getId());
        assertNull(a.getType());
    }

    @Test(expected=NullPointerException.class)
    public void SetNullId(){
        a.setId(null);
    }

    @Test
    public void testSetId(){
        a.setId(Long.MIN_VALUE);
        assertEquals(Long.MIN_VALUE, (long) a.getId());
    }

    @Test
    public void testSetType() {
        ActionType type = new ActionType();
        a.setType(type);
        assertEquals(type, a.getType());

    }

    @Test(expected = NullPointerException.class)
    public void testSetNullType() {
        a.setType(null);
    }

    @Test
    public void testSetMember() {
        ProjectMember pm = new ProjectMember();
        a.setMember(pm);
        assertEquals(pm, a.getMember());
    }

    @Test (expected = NullPointerException.class)
    public void testSetNullMember() {
        a.setMember(null);
    }

    @Test
    public void setToString(){
        assertTrue(TestUtil.toStringHelper(a));
    }

    @Test
    public void testHashCode(){
        Action ac = new Action();
        ac.setId(Long.MIN_VALUE);
        ac.setMember(new ProjectMember());
        ac.setType(new ActionType());
        
        Action ac1 = new Action();
        ac1.setId(Long.MIN_VALUE);
        ac1.setMember(new ProjectMember());
        ac1.setType(new ActionType());
        
        assertEquals(ac.hashCode(),ac1.hashCode());
        TestUtil.assertNotEquals(a.hashCode(), ac.hashCode());
    }

    @Test
    public void testEquals(){
        assertFalse(a.equals(new TestUtil()));
    }
}
