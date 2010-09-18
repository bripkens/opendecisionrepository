package nl.rug.search.odr.entities;

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

    @Test (expected=NullPointerException.class)
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
}
