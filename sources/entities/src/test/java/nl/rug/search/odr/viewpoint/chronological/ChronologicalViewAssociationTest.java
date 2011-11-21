package nl.rug.search.odr.viewpoint.chronological;

import nl.rug.search.odr.entities.Version;
import nl.rug.search.odr.entities.Iteration;
import java.util.ArrayList;
import java.util.List;
import nl.rug.search.odr.viewpoint.Handle;
import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;
import static nl.rug.search.odr.Assert.*;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
public class ChronologicalViewAssociationTest {

    private ChronologicalViewAssociation association;




    @Before
    public void setUp() {
        association = new ChronologicalViewAssociation();
    }




    @Test
    public void testInit() {
        assertNull(association.getId());
        assertNull(association.getSourceIteration());
        assertNull(association.getSourceVersion());
        assertNull(association.getTargetIteration());
        assertNull(association.getTargetVersion());
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
    public void testGetSetTargets() {
        Iteration sourceIteration = new Iteration();
        Iteration targetIteration = new Iteration();

        Version sourceVersion = new Version();
        Version targetVersion = new Version();

        association.setSourceVersion(sourceVersion);
        assertSame(sourceVersion, association.getSourceVersion());

        association.setSourceIteration(sourceIteration);
        assertSame(sourceIteration, association.getSourceIteration());

        association.setTargetVersion(targetVersion);
        assertSame(targetVersion, association.getTargetVersion());

        association.setTargetIteration(targetIteration);
        assertSame(targetIteration, association.getTargetIteration());
    }



    @Test
    public void testIsPersistable() {
        Iteration sourceIteration = new Iteration();
        Iteration targetIteration = new Iteration();

        Version sourceVersion = new Version();
        Version targetVersion = new Version();

        assertFalse(association.isPersistable());

        association.setSourceIteration(sourceIteration);

        assertFalse(association.isPersistable());

        association.setTargetIteration(targetIteration);

        assertTrue(association.isPersistable());

        association.setSourceVersion(sourceVersion);

        assertFalse(association.isPersistable());

        association.setSourceIteration(null);

        assertTrue(association.isPersistable());

        association.setTargetVersion(targetVersion);

        assertFalse(association.isPersistable());

        association.setTargetIteration(null);

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
