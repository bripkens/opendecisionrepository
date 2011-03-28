package nl.rug.search.odr.viewpoint.relationship;

import nl.rug.search.odr.viewpoint.relationship.InitRelationshipView;
import nl.rug.search.odr.viewpoint.relationship.RelationshipViewVisualization;
import nl.rug.search.odr.viewpoint.relationship.RelationshipViewAssociation;
import nl.rug.search.odr.entities.Relationship;
import java.util.Date;
import nl.rug.search.odr.entities.Decision;
import nl.rug.search.odr.entities.Project;
import nl.rug.search.odr.entities.Version;
import nl.rug.search.odr.viewpoint.AbstractAssociation;
import nl.rug.search.odr.viewpoint.Viewpoint;
import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
public class InitRelationshipViewTest {

    @Before
    public void setUp() {
    }




    @Test
    public void testGetView() {
        Viewpoint type = Viewpoint.RELATIONSHIP;
        Project project = new Project();

        Decision d1 = new Decision();
        d1.setName("aaa");
        project.addDecision(d1);

        Version v11 = new Version();
        v11.setId(11l);
        v11.setDecidedWhen(new Date());
        d1.addVersion(v11);

        Version v12 = new Version();
        v12.setId(12l);
        v12.setDecidedWhen(new Date(v11.getDecidedWhen().getTime() + 1));
        d1.addVersion(v12);






        Decision removedDecision = new Decision();
        removedDecision.setName("bbb");
        project.addDecision(removedDecision);

        Version removedVersion = new Version();
        removedVersion.setId(-1l);
        removedVersion.setRemoved(true);
        removedVersion.setDecidedWhen(new Date(v12.getDecidedWhen().getTime() + 1));
        removedDecision.addVersion(removedVersion);







        Decision d2 = new Decision();
        d2.setName("ccc");
        project.addDecision(d2);

        Version v21 = new Version();
        v21.setId(21l);
        v21.setDecidedWhen(new Date(removedVersion.getDecidedWhen().getTime() + 1));
        d2.addVersion(v21);

        Version v22 = new Version();
        v22.setId(22l);
        v22.setDecidedWhen(new Date(v21.getDecidedWhen().getTime() + 1));
        d2.addVersion(v22);







        Relationship r = new Relationship();
        r.setSource(v12);
        r.setTarget(v21);

        Relationship r2 = new Relationship();
        r2.setSource(v12);
        r2.setTarget(v22);










        InitRelationshipView irv = new InitRelationshipView(project);
        RelationshipViewVisualization v = irv.getView();

        assertSame(project, irv.getProject());
        assertFalse(v.containsVersion(v11));
        assertTrue(v.containsVersion(v12));
        assertFalse(v.containsVersion(v21));
        assertTrue(v.containsVersion(v22));
        assertFalse(v.containsVersion(removedVersion));

        assertFalse(containsRelationship(v, r));
        assertTrue(containsRelationship(v, r2));
    }




    private boolean containsRelationship(RelationshipViewVisualization v, Relationship r) {
        for (AbstractAssociation a : v.getAssociations()) {
            if (((RelationshipViewAssociation) a).getRelationship() == r) {
                return true;
            }
        }

        return false;
    }




    @Test
    public void testUpdateView() {
        Project project = new Project();

        Decision d1 = new Decision();
        d1.setName("aaa");
        project.addDecision(d1);

        Version v11 = new Version();
        v11.setId(11l);
        v11.setDecidedWhen(new Date());
        d1.addVersion(v11);

        Version v12 = new Version();
        v12.setId(12l);
        v12.setDecidedWhen(new Date(v11.getDecidedWhen().getTime() + 1));
        d1.addVersion(v12);






        Decision removedDecision = new Decision();
        removedDecision.setName("bbb");
        project.addDecision(removedDecision);

        Version removedVersion = new Version();
        removedVersion.setId(-1l);
        removedVersion.setRemoved(true);
        removedVersion.setDecidedWhen(new Date(v12.getDecidedWhen().getTime() + 1));
        removedDecision.addVersion(removedVersion);







        Decision d2 = new Decision();
        d2.setName("ccc");
        project.addDecision(d2);

        Version v21 = new Version();
        v21.setId(21l);
        v21.setDecidedWhen(new Date(removedVersion.getDecidedWhen().getTime() + 1));
        d2.addVersion(v21);

        Version v22 = new Version();
        v22.setId(22l);
        v22.setDecidedWhen(new Date(v21.getDecidedWhen().getTime() + 1));
        d2.addVersion(v22);







        Relationship r = new Relationship();
        r.setId(1l);
        r.setSource(v12);
        r.setTarget(v21);

        Relationship r2 = new Relationship();
        r2.setId(2l);
        r2.setSource(v12);
        r2.setTarget(v22);

        Relationship r3 = new Relationship();
        r3.setId(3l);
        r3.setSource(v22);
        r3.setTarget(v12);


        InitRelationshipView irv = new InitRelationshipView(project);
        RelationshipViewVisualization v = irv.getView();
        assertTrue(containsRelationship(v, r3));


        // testing to remove the current version of a decision
        v12.setRemoved(true);
        irv.updateView(v);
        assertTrue(v.containsVersion(v11));
        assertFalse(v.containsVersion(v12));
        assertFalse(containsRelationship(v, r2));
        assertFalse(containsRelationship(v, r3));

        // Adding a new version to a decision, which is already part of the visualization
        Version v13 = new Version();
        v13.setId(13l);
        v13.setDecidedWhen(new Date(v12.getDecidedWhen().getTime() + 1));
        d1.addVersion(v13);
        irv.updateView(v);
        assertFalse(v.containsVersion(v11));
        assertFalse(v.containsVersion(v12));
        assertTrue(v.containsVersion(v13));

        // testing to remove a whole decision
        v13.getDecision().remove();
        irv.updateView(v);
        assertFalse(v.containsVersion(v11));
        assertFalse(v.containsVersion(v12));
        assertFalse(v.containsVersion(v13));

        // adding a completely new decision
        Decision d3 = new Decision();
        d3.setName("ddd");
        project.addDecision(d3);

        Version v31 = new Version();
        v31.setId(31l);
        v31.setDecidedWhen(new Date());
        d3.addVersion(v31);


        irv.updateView(v);
        assertTrue(v.containsVersion(v31));


        // adding a new relationship to a version which is arleady part of the visualization
        v12.setRemoved(false);
        irv.updateView(v);
        assertTrue(v.containsVersion(v12));
        
        Relationship r4 = new Relationship();
        r4.setId(4l);
        r4.setSource(v31);
        r4.setTarget(v12);


        irv.updateView(v);
        assertTrue(v.containsVersion(v12));
        assertTrue(v.containsVersion(v31));
        assertTrue(containsRelationship(v, r4));


        d3.remove();
        irv.updateView(v);
        assertTrue(v.containsVersion(v12));
        assertFalse(v.containsVersion(v31));
        assertFalse(containsRelationship(v, r4));


        // adding a new decision with a new relationship
        Decision d4 = new Decision();
        d4.setName("eee");
        project.addDecision(d4);

        Version v41 = new Version();
        v41.setId(41l);
        v41.setDecidedWhen(new Date());
        d4.addVersion(v41);

        Relationship r5 = new Relationship();
        r5.setId(5l);
        r5.setSource(v12);
        r5.setTarget(v41);

        irv.updateView(v);
        assertTrue(v.containsVersion(v41));
        assertTrue(containsRelationship(v, r5));
    }




}



