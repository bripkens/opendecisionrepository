package nl.rug.search.odr.entities;

import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
public class ActionTest {

    private Person p;

    @Before
    public void setUp() {
        p = new Person();
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testInitialization() {
        assertNull(p.getPersonId());
        assertNull(p.getName());
        assertNull(p.getPassword());
        assertNull(p.getEmail());

        assertNotNull(p.getMemberships());

        assertTrue(p.getMemberships().isEmpty());
    }

    @Test
    public void testSetEmail() {
        p.setEmail("foo@foo.de");
    }

    @Test(expected=RuntimeException.class)
    public void testSetInvalidEmail1() {
        p.setEmail("dassa");
    }

    @Test(expected=RuntimeException.class)
    public void testSetInvalidEmail2() {
        p.setEmail("dassa@dasdsa");
    }

    @Test(expected=RuntimeException.class)
    public void testSetInvalidEmail3() {
        p.setEmail("dassadasdsa.de");
    }
}
