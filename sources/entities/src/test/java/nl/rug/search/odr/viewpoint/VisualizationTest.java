package nl.rug.search.odr.viewpoint;

import nl.rug.search.odr.entities.Version;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;
import static nl.rug.search.odr.Assert.*;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
public class VisualizationTest {

    private Visualization v;




    @Before
    public void setUp() {
        v = new Visualization();
    }




    @Test
    public void testInit() {
        assertNull(v.getId());
        assertNull(v.getName());
        assertTrue(v.getAssociations().isEmpty());
        assertTrue(v.getNodes().isEmpty());
        assertNull(v.getDocumentedWhen());
        assertNull(v.getType());
    }




    @Test
    public void testId() {
        long id = 5l;
        v.setId(id);

        assertEquals(id, (long) v.getId());
    }




    @Test
    public void testName() {
        String name = "Some relationship view";

        v.setName(name);

        assertEquals(name, v.getName());
    }




    @Test
    public void testSetDocumentedWhen() {
        Date now = new Date();
        v.setDocumentedWhen(now);

        assertEquals(now, v.getDocumentedWhen());
    }




    @Test
    public void testViewpoint() {
        Viewpoint type = Viewpoint.CHRONOLOGICAL;

        v.setType(type);

        assertEquals(type, v.getType());
    }




    @Test
    public void testNodes() {
        Node n1 = new Node();
        Node n2 = new Node();

        v.addNode(n1);

        assertTrue(containsReference(v.getNodes(), n1));
        assertFalse(containsReference(v.getNodes(), n2));

        v.addNode(n2);

        assertTrue(containsReference(v.getNodes(), n1));
        assertTrue(containsReference(v.getNodes(), n2));

        v.removeNode(n1);

        assertFalse(containsReference(v.getNodes(), n1));
        assertTrue(containsReference(v.getNodes(), n2));

        v.removeNode(n2);

        assertFalse(containsReference(v.getNodes(), n1));
        assertFalse(containsReference(v.getNodes(), n2));

        List<Node> nodes = new ArrayList<Node>();
        nodes.add(n1);

        v.setNodes(nodes);

        assertTrue(containsReference(v.getNodes(), n1));
        assertFalse(containsReference(v.getNodes(), n2));

        v.removeAllNodes();

        assertFalse(containsReference(v.getNodes(), n1));
        assertFalse(containsReference(v.getNodes(), n2));

        v.addNode(n2);

        assertFalse(containsReference(v.getNodes(), n1));
        assertTrue(containsReference(v.getNodes(), n2));

        nodes = v.getNodes();

        assertFalse(containsReference(nodes, n1));
        assertTrue(containsReference(nodes, n2));
    }




    @Test
    public void testAssociations() {
        Association a1 = new Association();
        Association a2 = new Association();

        v.addAssociation(a1);

        assertTrue(containsReference(v.getAssociations(), a1));
        assertFalse(containsReference(v.getAssociations(), a2));

        v.addAssociation(a2);

        assertTrue(containsReference(v.getAssociations(), a1));
        assertTrue(containsReference(v.getAssociations(), a2));

        v.removeAssociation(a1);

        assertFalse(containsReference(v.getAssociations(), a1));
        assertTrue(containsReference(v.getAssociations(), a2));

        v.removeAssociation(a2);

        assertFalse(containsReference(v.getAssociations(), a1));
        assertFalse(containsReference(v.getAssociations(), a2));

        List<Association> associations = new ArrayList<Association>();
        associations.add(a1);

        v.setAssociations(associations);

        assertTrue(containsReference(v.getAssociations(), a1));
        assertFalse(containsReference(v.getAssociations(), a2));

        v.removeAllAssociations();

        assertFalse(containsReference(v.getAssociations(), a1));
        assertFalse(containsReference(v.getAssociations(), a2));

        v.addAssociation(a2);

        assertFalse(containsReference(v.getAssociations(), a1));
        assertTrue(containsReference(v.getAssociations(), a2));

        associations = v.getAssociations();

        assertFalse(containsReference(associations, a1));
        assertTrue(containsReference(associations, a2));
    }




    @Test
    public void testIsPersistable() {
        Date now = new Date();
        Viewpoint type = Viewpoint.RELATIONSHIP;

        assertFalse(v.isPersistable());

        v.setDocumentedWhen(now);

        assertFalse(v.isPersistable());

        v.setType(type);

        assertFalse(v.isPersistable());

        v.addNode(new Node());

        assertTrue(v.isPersistable());
    }




    @Test
    public void testIsPersistableOtherway() {
        Date now = new Date();
        Viewpoint type = Viewpoint.RELATIONSHIP;

        assertFalse(v.isPersistable());

        v.setDocumentedWhen(now);

        assertFalse(v.isPersistable());

        v.addNode(new Node());

        assertFalse(v.isPersistable());

        v.setType(type);

        assertTrue(v.isPersistable());
    }




    @Test
    public void testCompareData() {
        String name = "someDiagram";
        Date now = new Date();
        Viewpoint type = Viewpoint.STAKEHOLDER_INVOLVEMENT;

        v.setDocumentedWhen(now);
        v.setName(name);
        v.setType(type);

        Object[] data = v.getCompareData();

        assertEquals(now, data[0]);
        assertEquals(name, data[1]);
        assertEquals(type, data[2]);
    }




    @Test
    public void testContainsVersion() {
        Node n1 = new Node();
//        n1.setId(1l);
        Version v1 = new Version();
//        v1.setId(1l);
        n1.setVersion(v1);

        Node n2 = new Node();
//        n2.setId(2l);
        Version v2 = new Version();
//        v2.setId(2l);
        n2.setVersion(v2);

        assertSame(n1.getVersion(), v1);
        assertSame(n2.getVersion(), v2);
        assertNotSame(n1, n2);
        assertNotSame(n1.getVersion(), n2.getVersion());
        assertNotSame(v2, v1);

        assertFalse(v.containsVersion(v2));
        assertFalse(v.containsVersion(v2));

        v.addNode(n1);

        assertTrue(v.containsVersion(v1));
        assertFalse(v.containsVersion(v2));

        v.addNode(n2);

        assertTrue(v.containsVersion(v1));
        assertTrue(v.containsVersion(v2));

        v.removeNode(n2);

        assertFalse(v.containsVersion(v2));
        assertTrue(v.containsVersion(v1));
        
    }
}
