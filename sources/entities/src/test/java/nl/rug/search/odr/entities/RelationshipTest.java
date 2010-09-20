package nl.rug.search.odr.entities;

import nl.rug.search.odr.BusinessException;
import nl.rug.search.odr.TestUtil;
import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static nl.rug.search.odr.TestUtil.*;

/**
 *
 * @author Stefan
 */
public class RelationshipTest {

    private Relationship r;

    @Before
    public void setUp() {
        r = new Relationship();
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testInitialization() {
        assertNull(r.getId());
        assertNull(r.getSource());
        assertNull(r.getTarget());
    }


    @Test
    public void testId() {
        r.setId(Long.MIN_VALUE);

        assertEquals(Long.MIN_VALUE, (long) r.getId());
    }

    @Test (expected=BusinessException.class)
    public void testNullId(){
        r.setId(null);
    }

    @Test
    public void testSetSouceVersion() {
        Version v = new Version();

        r.setSourceVersion(v);

        assertSame(v, r.getSource());
        assertNotSame(v, r.getTarget());
        
        assertTrue(containsReference(v.getRelationships(), r));
        assertEquals(r.getSource(),v);
    }

    @Test
    public void testSetDestinationVersion() {
        Version v = new Version();

        r.setTargetVersion(v);

        assertSame(v, r.getTarget());
        assertNotSame(v, r.getSource());

        assertFalse(containsReference(v.getRelationships(), r));
    }

    @Test
    public void testToString(){
        assertTrue(TestUtil.toStringHelper(r));
    }

    @Test
    public void testHashCode(){
        Relationship r2 = new Relationship();
        r2.setId(Long.MIN_VALUE);
        r2.setSourceVersion(new Version());
        r2.setTargetVersion(new Version());

        Relationship r3 = new Relationship();
        r3.setId(Long.MIN_VALUE);
        r3.setSourceVersion(new Version());
        r3.setTargetVersion(new Version());
  

        assertEquals(r2.hashCode(),r3.hashCode());
        TestUtil.assertNotEquals(r.hashCode(), r2.hashCode());
    }

    @Test
    public void testEquals(){
        assertFalse(r.equals(new TestUtil()));

        Relationship r2 = new Relationship();
        r2.setId(Long.MIN_VALUE);
        r2.setSourceVersion(new Version());
        r2.setTargetVersion(new Version());

        Relationship r3 = new Relationship();
        r3.setId(Long.MAX_VALUE);
        r3.setSourceVersion(new Version());
        r3.setTargetVersion(new Version());

        assertFalse(r2.equals(r3));

        assertTrue(r.equals(r));
    }

    @Test
    public void testNullEquals(){
        assertFalse(r.equals(null));
    }


}
