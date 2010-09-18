package nl.rug.search.odr.entities;

import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Stefan
 */
public class ArchitecturalDecisionTest {

    private ArchitecturalDecision a;

    @Before
    public void setUp() {
        a = new ArchitecturalDecision();
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testInitialization() {
        assertNull(a.getId());
        assertNull(a.getName());
        assertNull(a.getOprId());
        assertNull(a.getProblem());
        assertNull(a.getVersion());
        assertNotNull(a.getVersions());

        assertTrue(a.getVersions().isEmpty());


    }

    @Test
    public void testSetId() {
        a.setId(Long.MIN_VALUE);
        assertEquals(Long.MIN_VALUE, (long) a.getId());
    }

    @Test(expected = NullPointerException.class)
    public void testSetNullId() {
        a.setId(null);
    }

    @Test
    public void testSetName() {
        a.setName("foo");
        assertEquals("foo", a.getName());
    }

    @Test(expected = NullPointerException.class)
    public void testNullName() {
       a.setName(null);
    }

    @Test(expected = RuntimeException.class)
    public void testEmptyName() {
        a.setName("        ");
    }

     @Test
    public void testSetOprId() {
        a.setOprId("foo");
        assertEquals("foo", a.getOprId());
    }

    @Test(expected = NullPointerException.class)
    public void testNullOprId() {
       a.setOprId(null);
    }

      @Test
    public void testSetProblem() {
        a.setProblem("foo");
        assertEquals("foo", a.getProblem());
    }

    @Test(expected = NullPointerException.class)
    public void testNullProblem() {
       a.setProblem(null);
    }

    @Test
    public void testSetVersion() {
        Version v = new Version();
        a.setVersion(v);
        assertEquals(v, a.getVersion());
    }

    @Test(expected = NullPointerException.class)
    public void testNullVersion() {
       a.setVersion(null);
    }

        @Test
    public void testSetDecision() {
        String decision = "foo";
        a.setDecision(decision);
        assertEquals(decision, a.getDecision());
    }

    @Test(expected = NullPointerException.class)
    public void testNullDecision() {
       a.setDecision(null);
    }

        @Test
    public void testSetArguments() {
        String arguments = "foo";
        a.setArguments(arguments);
        assertEquals(arguments, a.getArguments());
    }

    @Test(expected = NullPointerException.class)
    public void testNullArguments() {
       a.setArguments(null);
    }
    
}
