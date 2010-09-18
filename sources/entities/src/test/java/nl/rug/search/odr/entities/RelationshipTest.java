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
        throw new UnsupportedOperationException();
    }

    @Test
    public void testSetSouceVersion() {
        Version v = new Version();

        r.setSourceVersion(v);

        assertSame(v, r.getSource());
        assertNotSame(v, r.getTarget());
        
        assertTrue(containsReference(v.getRelationships(), r));
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
