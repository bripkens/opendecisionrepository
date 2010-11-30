package nl.rug.search.odr.viewpoint;

import java.util.ArrayList;
import java.util.List;
import nl.rug.search.odr.entities.Relationship;
import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;
import static nl.rug.search.odr.Assert.*;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
public class AssociationTest {

    private Association association;




    @Before
    public void setUp() {
        association = new Association();
    }




    @Test
    public void testInit() {
        assertNull(association.getId());
        assertNull(association.getRelationship());
        assertTrue(association.getHandles().isEmpty());
        assertEquals(0, association.getCompareData().length);
    }




    @Test
    public void testId() {
        association.setId(1l);
        assertEquals(1l, (long) association.getId());
    }




    @Test
    public void testHandles() {
        Handle h = new Handle();
        association.addHandle(h);
        assertTrue(containsReference(association.getHandles(), h));

        Handle h2 = new Handle();
        association.addHandle(h2);
        assertTrue(containsReference(association.getHandles(), h2));

        association.removeHandle(h);
        assertFalse(containsReference(association.getHandles(), h));

        assertTrue(containsReference(association.getHandles(), h2));

        association.removeAllHandles();

        assertFalse(containsReference(association.getHandles(), h2));


        List<Handle> handles = new ArrayList<Handle>();
        association.setHandles(handles);
        handles.add(h);
        assertTrue(containsReference(association.getHandles(), h));
        assertTrue(containsReference(handles, h));
    }




    @Test
    public void testRelationship() {
        Relationship r = new Relationship();
        association.setRelationship(r);
        assertSame(r, association.getRelationship());
    }



    @Test
    public void testIsPersistable() {
        assertFalse(association.isPersistable());

        Relationship r = new Relationship();
        association.setRelationship(r);

        assertTrue(association.isPersistable());
    }



    @Test
    public void testLabelPosition() {
        association.setLabelX(5);
        association.setLabelY(10);

        assertEquals(5, association.getLabelX());
        assertEquals(10, association.getLabelY());
    }
}
