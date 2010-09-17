package nl.rug.search.odr.entities;

import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
public class DecisionTest {

    private Decision d;

    @Before
    public void setUp() {
        d = new Decision();
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testInitialization() {
        assertNull(d.getArguments());
        assertNull(d.getDecision());
        assertNull(d.getOprId());
        assertNull(d.getProblem());
        assertNull(d.getName());
    }

    @Test
    public void testSetname() {
        d.setName("foo");
        assertEquals("foo", d.getName());
    }

    @Test
    public void testSetProblem() {
        d.setProblem("foo");
        assertEquals("foo", d.getProblem());
    }

    @Test
    public void testSetDecision() {
        d.setDecision("foo");
        assertEquals("foo", d.getDecision());
    }

    @Test
    public void testSetArguments() {
        d.setArguments("foo");
        assertEquals("foo", d.getArguments());
    }
}
