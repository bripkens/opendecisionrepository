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

    @Test
    public void testaddVerison(){
        Version v = new Version();
        Version v2 = new Version();
        a.addVerison(v);
        assertTrue(TestUtil.containsReference(a.getVersions(),v));
        assertFalse(TestUtil.containsReference(a.getVersions(),v2));
    }
    
    @Test (expected = NullPointerException.class)
    public void testNullAddVerison(){
        a.addVerison(null);

    }

    @Test
    public void setToString(){
        assertTrue(TestUtil.toStringHelper(a));
    }

    @Test
    public void testHashCode(){
        ArchitecturalDecision at = new ArchitecturalDecision();
        at.setId(Long.MIN_VALUE);
        at.setArguments("bla");
        at.setName("bla");
        at.setDecision("bla");
        at.setProblem("bla");
        at.setOprId("bla");
        at.setVersion(new Version());

        ArchitecturalDecision at1 = new ArchitecturalDecision();
        at1.setId(Long.MIN_VALUE);
        at1.setArguments("bla");
        at1.setName("bla");
        at1.setDecision("bla");
        at1.setProblem("bla");
        at1.setOprId("bla");
        at1.setVersion(new Version());

        assertEquals(at.hashCode(),at1.hashCode());
        TestUtil.assertNotEquals(a.hashCode(), at.hashCode());
    }

    @Test
    public void testEquals(){
        assertFalse(a.equals(new TestUtil()));

        ArchitecturalDecision at1 = new ArchitecturalDecision();
        at1.setId(Long.MIN_VALUE);
        at1.setArguments("bla");
        at1.setName("bla");
        at1.setDecision("bla");
        at1.setProblem("bla");
        at1.setOprId("bla");
        at1.setVersion(new Version());

        ArchitecturalDecision at2 = new ArchitecturalDecision();
        at2.setId(Long.MIN_VALUE);
        at2.setArguments("foo");
        at2.setName("foo");
        at2.setDecision("foo");
        at2.setProblem("foo");
        at2.setOprId("foo");
        at2.setVersion(new Version());

        assertFalse(at1.equals(at2));
        
        assertTrue(a.equals(a));
    }

    @Test
    public void testNullEquals(){
        assertFalse(a.equals(null));
    }
    
}
