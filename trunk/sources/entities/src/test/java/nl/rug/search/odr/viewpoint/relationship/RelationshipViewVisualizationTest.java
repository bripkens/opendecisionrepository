package nl.rug.search.odr.viewpoint.relationship;

import nl.rug.search.odr.viewpoint.relationship.RelationshipViewVisualization;
import nl.rug.search.odr.viewpoint.relationship.RelationshipViewAssociation;
import nl.rug.search.odr.entities.Version;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import nl.rug.search.odr.viewpoint.Node;
import nl.rug.search.odr.viewpoint.Viewpoint;
import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;
import static nl.rug.search.odr.Assert.*;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
public class RelationshipViewVisualizationTest {

    private RelationshipViewVisualization v;




    @Before
    public void setUp() {
        v = new RelationshipViewVisualization();
    }




    @Test
    public void testInit() {
        assertNull(v.getId());
        assertNull(v.getName());
        assertTrue(v.getAssociations().isEmpty());
        assertTrue(v.getNodes().isEmpty());
        assertNull(v.getDocumentedWhen());
        assertEquals(Viewpoint.RELATIONSHIP, v.getType());
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
        RelationshipViewAssociation a1 = new RelationshipViewAssociation();
        RelationshipViewAssociation a2 = new RelationshipViewAssociation();

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

        List<RelationshipViewAssociation> associations = new ArrayList<RelationshipViewAssociation>();
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

        v.addNode(new Node());

        assertTrue(v.isPersistable());
    }




    @Test
    public void testIsPersistableOtherway() {
        Date now = new Date();

        assertFalse(v.isPersistable());

        v.setDocumentedWhen(now);

        assertFalse(v.isPersistable());

        v.addNode(new Node());

        assertTrue(v.isPersistable());
    }




    @Test
    public void testCompareData() {
        String name = "someDiagram";
        Date now = new Date();

        v.setDocumentedWhen(now);
        v.setName(name);

        RelationshipViewVisualization v2 = new RelationshipViewVisualization();
        v2.setDocumentedWhen(now);
        v2.setName(name);

        assertEquals(v2, v);
    }




    @Test
    public void testContainsVersion() {
        v = new RelationshipViewVisualization();

        Node n1 = new Node();
        n1.setVisible(true);
        n1.setVersion(new Version());
        n1.getVersion().setDecidedWhen(new Date());

        Node n2 = new Node();
        n2.setVisible(false);
        n2.setVersion(new Version());
        n2.getVersion().setDecidedWhen(new Date(new Date().getTime() + 10));

        assertFalse(v.containsVersion(n1.getVersion()));
        assertFalse(v.containsVersion(n2.getVersion()));

        v.addNode(n1);

        assertTrue(v.containsVersion(n1.getVersion()));
        assertFalse(v.containsVersion(n2.getVersion()));

        v.addNode(n2);

        assertTrue(v.containsVersion(n1.getVersion()));
        assertTrue(v.containsVersion(n2.getVersion()));

        v.removeNode(n2);

        assertFalse(v.containsVersion(n2.getVersion()));
        assertTrue(v.containsVersion(n1.getVersion()));
        
    }
}
