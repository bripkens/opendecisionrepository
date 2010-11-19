package nl.rug.search.odr.entities;

import nl.rug.search.odr.BusinessException;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import static nl.rug.search.odr.Assert.*;
/**
 *
 * @author Ben
 */
public class RelationshipTest {

    private Relationship r;




    @Before
    public void setUp() {
        r = new Relationship();
    }




    @Test
    public void testInitialization() {
        assertNull(r.getId());
        assertNull(r.getTarget());
        assertNull(r.getType());
        assertFalse(r.isPersistable());
    }




    @Test
    public void testId() {
        r.setId(Long.MIN_VALUE);

        assertEquals(Long.MIN_VALUE, (long) r.getId());
    }




    @Test(expected = BusinessException.class)
    public void testNullId() {
        r.setId(null);
    }




    @Test
    public void testSetSource() {
        Version source = new Version();

        r.setSource(source);

        assertSame(source, r.getSource());

        containsReference(source.getOutgoingRelationships(), r);
    }



    



    @Test
    public void testSetTarget() {
        Version target = new Version();

        r.setTarget(target);

        assertSame(target, r.getTarget());
    }




    @Test(expected = BusinessException.class)
    public void testSetTargetNull() {
        r.setTarget(null);
    }




    @Test
    public void testSetType() {
        RelationshipType target = new RelationshipType();

        r.setType(target);

        assertSame(target, r.getType());
    }




    @Test(expected = BusinessException.class)
    public void testSetTypeNull() {
        r.setType(null);
    }




    @Test
    public void testIsPersistable() {
        assertFalse(r.isPersistable());

        r.setTarget(new Version());

        assertFalse(r.isPersistable());

        r.setType(new RelationshipType());

        assertFalse(r.isPersistable());


        r.setSource(new Version());

        assertTrue(r.isPersistable());
    }




    @Test
    public void testIsPersistableOtherwy() {
        assertFalse(r.isPersistable());

        r.setType(new RelationshipType());

        assertFalse(r.isPersistable());

        r.setSource(new Version());

        assertFalse(r.isPersistable());

        r.setTarget(new Version());

        assertTrue(r.isPersistable());
    }




    @Test
    public void testGetCompareData() {
        RelationshipType type = new RelationshipType();

        r.setType(type);

        assertSame(type, r.getCompareData()[0]);
    }
}
