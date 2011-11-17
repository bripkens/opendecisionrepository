package nl.rug.search.odr.viewpoint.chronological;

import org.junit.Test;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import nl.rug.search.odr.entities.Decision;
import nl.rug.search.odr.entities.Iteration;
import nl.rug.search.odr.entities.Project;
import nl.rug.search.odr.entities.ProjectMember;
import nl.rug.search.odr.entities.Version;
import org.eclipse.persistence.history.AsOfClause;

import static org.junit.Assert.*;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
public class InitChronologicalViewTest {

    @Test
    public void testInitView() {
        Project p = new Project();

        ProjectMember pm1 = new ProjectMember();
        pm1.setId(1l);
        p.addMember(pm1);
        ProjectMember pm2 = new ProjectMember();
        pm2.setId(2l);
        p.addMember(pm2);

        // check that a visualization can be created even without any nodes
        InitChronologicalView icv = new InitChronologicalView(p);
        ChronologicalViewVisualization visual = icv.getView();

        assertTrue(visual.getNodes().isEmpty());
        assertTrue(visual.getAssociations().isEmpty());
        assertSame(p, icv.getProject());



        // check that a visualization can be created with one iteration
        Iteration i1 = new Iteration();
        i1.setName("iii1");
        i1.setStartDate(new Date(1));
        i1.setEndDate(new Date(10));
        p.addIteration(i1);

        icv = new InitChronologicalView(p);
        visual = icv.getView();

        assertTrue(containsNodeWith(visual, i1, null, true));
        assertEquals(1, visual.getNodes().size());



        // check that a visualization can be created with two iterations but with no versions
        Iteration iTemp = new Iteration();
        iTemp.setName("iiiTemp");
        iTemp.setStartDate(new Date(11));
        iTemp.setEndDate(new Date(20));
        p.addIteration(iTemp);

        icv = new InitChronologicalView(p);
        visual = icv.getView();
        
        assertTrue(containsNodeWith(visual, i1, null, false));
        assertTrue(containsNodeWith(visual, iTemp, null, true));
        assertTrue(containsAssociationWith(visual, i1, null, iTemp, null));
        p.removeIteration(iTemp);



        // Verify that a version will be added to the visualization when the version was created during
        // an iteration
        Decision d1 = new Decision();
        d1.setName("ddd1");
        Version v1_1 = new Version();
        v1_1.addInitiator(pm1);
        v1_1.setDecidedWhen(new Date(5));
        d1.addVersion(v1_1);
        p.addDecision(d1);

        icv = new InitChronologicalView(p);
        visual = icv.getView();

        assertTrue(containsNodeWith(visual, i1, null, false));
        assertTrue(containsNodeWith(visual, null, v1_1, true));
        assertTrue(containsAssociationWith(visual, i1, null, null, v1_1));
        assertEquals(2, visual.getNodes().size());



        // verify that a version which has not been created during an iteration is in the visualization and marked
        // as disconnected
        v1_1.setDecidedWhen(new Date(11));

        icv = new InitChronologicalView(p);
        visual = icv.getView();

        assertTrue(containsNodeWith(visual, i1, null, true));
        assertTrue(containsNodeWith(visual, null, v1_1, false, true));
        assertFalse(containsAssociationWith(visual, i1, null, null, v1_1));



        // verify that a disconnected and removed version is not in the visualization
        v1_1.setRemoved(true);

        icv = new InitChronologicalView(p);
        visual = icv.getView();

        assertTrue(containsNodeWith(visual, i1, null, true));
        assertFalse(containsNodeWith(visual, null, v1_1, false, true));
        assertFalse(containsAssociationWith(visual, i1, null, null, v1_1));
        v1_1.setRemoved(false);

        

        // verify that a version will be connected to the next iteration
        v1_1.setDecidedWhen(new Date(5));
        Iteration i2 = new Iteration();
        i2.setName("iii2");
        i2.setStartDate(new Date(11));
        i2.setEndDate(new Date(20));
        p.addIteration(i2);

        icv = new InitChronologicalView(p);
        visual = icv.getView();

        assertTrue(containsNodeWith(visual, i1, null, false));
        assertTrue(containsNodeWith(visual, null, v1_1, false));
        assertTrue(containsNodeWith(visual, i2, null, true));
        assertTrue(containsAssociationWith(visual, i1, null, null, v1_1));
        assertTrue(containsAssociationWith(visual, null, v1_1, i2, null));



        // verify that a version will be connected to some other version
        Decision d2 = new Decision();
        d2.setName("ddd2");
        Version v2_1 = new Version();
        v2_1.addInitiator(pm1);
        v2_1.setDecidedWhen(new Date(6));
        d2.addVersion(v2_1);
        p.addDecision(d2);

        icv = new InitChronologicalView(p);
        visual = icv.getView();

        assertTrue(containsNodeWith(visual, i1, null, false));
        assertTrue(containsNodeWith(visual, null, v1_1, false));
        assertTrue(containsNodeWith(visual, null, v2_1, false));
        assertTrue(containsNodeWith(visual, i2, null, true));
        assertTrue(containsAssociationWith(visual, i1, null, null, v1_1));
        assertTrue(containsAssociationWith(visual, null, v1_1, null, v2_1));
        assertTrue(containsAssociationWith(visual, null, v2_1, i2, null));



        // verify that a version can be added to some other iteration
        Decision d3 = new Decision();
        d3.setName("ddd3");
        Version v3_1 = new Version();
        v3_1.addInitiator(pm1);
        v3_1.setDecidedWhen(new Date(15));
        d3.addVersion(v3_1);
        p.addDecision(d3);

        icv = new InitChronologicalView(p);
        visual = icv.getView();

        assertTrue(containsNodeWith(visual, i1, null, false));
        assertTrue(containsNodeWith(visual, null, v1_1, false));
        assertTrue(containsNodeWith(visual, null, v2_1, false));
        assertTrue(containsNodeWith(visual, i2, null, false));
        assertTrue(containsNodeWith(visual, null, v3_1, true));
        assertTrue(containsAssociationWith(visual, i1, null, null, v1_1));
        assertTrue(containsAssociationWith(visual, null, v1_1, null, v2_1));
        assertTrue(containsAssociationWith(visual, null, v2_1, i2, null));
        assertTrue(containsAssociationWith(visual, i2, null, null, v3_1));



        // verify that a second version for a decision will also be in the chronological view
        Version v2_2 = new Version();
        v2_2.addInitiator(pm1);
        v2_2.setDecidedWhen(new Date(16));
        d2.addVersion(v2_2);

        icv = new InitChronologicalView(p);
        visual = icv.getView();

        assertTrue(containsNodeWith(visual, i1, null, false));
        assertTrue(containsNodeWith(visual, null, v1_1, false));
        assertTrue(containsNodeWith(visual, null, v2_1, false));
        assertTrue(containsNodeWith(visual, i2, null, false));
        assertTrue(containsNodeWith(visual, null, v3_1, false));
        assertTrue(containsNodeWith(visual, null, v2_2, true));
        assertTrue(containsAssociationWith(visual, i1, null, null, v1_1));
        assertTrue(containsAssociationWith(visual, null, v1_1, null, v2_1));
        assertTrue(containsAssociationWith(visual, null, v2_1, i2, null));
        assertTrue(containsAssociationWith(visual, i2, null, null, v3_1));
        assertTrue(containsAssociationWith(visual, null, v3_1, null, v2_2));



        // verify that a second group will be added
        Version v1_2 = new Version();
        v1_2.addInitiator(pm2);
        v1_2.setDecidedWhen(new Date(16));
        d1.addVersion(v1_2);

        icv = new InitChronologicalView(p);
        visual = icv.getView();

        assertTrue(containsNodeWith(visual, i1, null, false));
        assertTrue(containsNodeWith(visual, null, v1_1, false));
        assertTrue(containsNodeWith(visual, null, v2_1, false));
        assertTrue(containsNodeWith(visual, i2, null, false));
        assertTrue(containsNodeWith(visual, null, v3_1, false));
        assertTrue(containsNodeWith(visual, null, v2_2, true));
        assertTrue(containsNodeWith(visual, null, v1_2, true));
        assertTrue(containsAssociationWith(visual, i1, null, null, v1_1));
        assertTrue(containsAssociationWith(visual, null, v1_1, null, v2_1));
        assertTrue(containsAssociationWith(visual, null, v2_1, i2, null));
        assertTrue(containsAssociationWith(visual, i2, null, null, v3_1));
        assertTrue(containsAssociationWith(visual, i2, null, null, v1_2));
        assertTrue(containsAssociationWith(visual, null, v3_1, null, v2_2));



        // verify that all previous endpoints will be connected to the new iteration
        Iteration i3 = new Iteration();
        i3.setName("iii1");
        i3.setStartDate(new Date(21));
        i3.setEndDate(new Date(30));
        p.addIteration(i3);

        icv = new InitChronologicalView(p);
        visual = icv.getView();

        assertTrue(containsNodeWith(visual, i1, null, false));
        assertTrue(containsNodeWith(visual, null, v1_1, false));
        assertTrue(containsNodeWith(visual, null, v2_1, false));
        assertTrue(containsNodeWith(visual, i2, null, false));
        assertTrue(containsNodeWith(visual, null, v3_1, false));
        assertTrue(containsNodeWith(visual, null, v2_2, false));
        assertTrue(containsNodeWith(visual, null, v1_2, false));
        assertTrue(containsNodeWith(visual, i3, null, true));
        assertTrue(containsAssociationWith(visual, i1, null, null, v1_1));
        assertTrue(containsAssociationWith(visual, null, v1_1, null, v2_1));
        assertTrue(containsAssociationWith(visual, null, v2_1, i2, null));
        assertTrue(containsAssociationWith(visual, i2, null, null, v3_1));
        assertTrue(containsAssociationWith(visual, i2, null, null, v1_2));
        assertTrue(containsAssociationWith(visual, null, v3_1, null, v2_2));
        assertTrue(containsAssociationWith(visual, null, v2_2, i3, null));
        assertTrue(containsAssociationWith(visual, null, v1_2, i3, null));



        // verify that more than one version can be added without an iteration that follows them
        Version v1_3 = new Version();
        v1_3.addInitiator(pm2);
        v1_3.setDecidedWhen(new Date(22));
        d1.addVersion(v1_3);

        Version v1_4 = new Version();
        v1_4.addInitiator(pm2);
        v1_4.setDecidedWhen(new Date(23));
        d1.addVersion(v1_4);

        icv = new InitChronologicalView(p);
        visual = icv.getView();

        assertTrue(containsNodeWith(visual, i1, null, false));
        assertTrue(containsNodeWith(visual, null, v1_1, false));
        assertTrue(containsNodeWith(visual, null, v2_1, false));
        assertTrue(containsNodeWith(visual, i2, null, false));
        assertTrue(containsNodeWith(visual, null, v3_1, false));
        assertTrue(containsNodeWith(visual, null, v2_2, false));
        assertTrue(containsNodeWith(visual, null, v1_2, false));
        assertTrue(containsNodeWith(visual, i3, null, false));
        assertTrue(containsNodeWith(visual, null, v1_3, false));
        assertTrue(containsNodeWith(visual, null, v1_4, true));
        assertTrue(containsAssociationWith(visual, i1, null, null, v1_1));
        assertTrue(containsAssociationWith(visual, null, v1_1, null, v2_1));
        assertTrue(containsAssociationWith(visual, null, v2_1, i2, null));
        assertTrue(containsAssociationWith(visual, i2, null, null, v3_1));
        assertTrue(containsAssociationWith(visual, i2, null, null, v1_2));
        assertTrue(containsAssociationWith(visual, null, v3_1, null, v2_2));
        assertTrue(containsAssociationWith(visual, null, v2_2, i3, null));
        assertTrue(containsAssociationWith(visual, null, v1_2, i3, null));
        assertTrue(containsAssociationWith(visual, i3, null, null, v1_3));
        assertTrue(containsAssociationWith(visual, null, v1_3, null, v1_4));




        // verify that a remove version is not part of the visualization
        v1_4.setRemoved(true);

        icv = new InitChronologicalView(p);
        visual = icv.getView();

        assertTrue(containsNodeWith(visual, i1, null, false));
        assertTrue(containsNodeWith(visual, null, v1_1, false));
        assertTrue(containsNodeWith(visual, null, v2_1, false));
        assertTrue(containsNodeWith(visual, i2, null, false));
        assertTrue(containsNodeWith(visual, null, v3_1, false));
        assertTrue(containsNodeWith(visual, null, v2_2, false));
        assertTrue(containsNodeWith(visual, null, v1_2, false));
        assertTrue(containsNodeWith(visual, i3, null, false));
        assertTrue(containsNodeWith(visual, null, v1_3, true));



        // verify that a removed decision is not part of the visualization
        d1.remove();

        icv = new InitChronologicalView(p);
        visual = icv.getView();

        assertTrue(containsNodeWith(visual, i1, null, false));
        assertTrue(containsNodeWith(visual, null, v2_1, false));
        assertTrue(containsNodeWith(visual, i2, null, false));
        assertTrue(containsNodeWith(visual, null, v3_1, false));
        assertTrue(containsNodeWith(visual, null, v2_2, false));
        assertTrue(containsNodeWith(visual, i3, null, true));
    }




    private boolean containsNodeWith(ChronologicalViewVisualization v, Iteration i, Version version, boolean endpoint) {
        return containsNodeWith(v, i, version, endpoint, false);
    }


    private boolean containsNodeWith(ChronologicalViewVisualization v, Iteration i, Version version, boolean endpoint, boolean disconnected) {
        return v.getNode(i, version, endpoint, disconnected) != null;
    }


    private boolean containsAssociationWith(ChronologicalViewVisualization v, Iteration sourceIteration,
            Version sourceVersion, Iteration targetIteration, Version targetVersion) {

        return v.getAssociation(sourceIteration, sourceVersion, targetIteration, targetVersion) != null;
    }




}



