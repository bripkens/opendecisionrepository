
package nl.rug.search.odr.viewpoint.chronological;

import nl.rug.search.odr.viewpoint.Handle;
import java.util.Date;
import nl.rug.search.odr.entities.Decision;
import nl.rug.search.odr.entities.Iteration;
import nl.rug.search.odr.entities.Project;
import nl.rug.search.odr.entities.ProjectMember;
import nl.rug.search.odr.entities.Version;
import org.junit.Test;
import static org.junit.Assert.*;
/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
public class UpdateChronologicalViewTest {


    @Test
    public void testGetView() {
        Project p = new Project();

        ProjectMember pm1 = new ProjectMember();
        pm1.setId(1l);
        p.addMember(pm1);
        ProjectMember pm2 = new ProjectMember();
        pm2.setId(2l);
        p.addMember(pm2);

        Iteration i1 = new Iteration();
        i1.setName("iii1");
        i1.setStartDate(new Date(1));
        i1.setEndDate(new Date(10));
        p.addIteration(i1);

        Decision d1 = new Decision();
        d1.setName("ddd1");
        Version v1_1 = new Version();
        v1_1.addInitiator(pm1);
        v1_1.setDecidedWhen(new Date(5));
        d1.addVersion(v1_1);
        p.addDecision(d1);

        InitChronologicalView icv = new InitChronologicalView(p);
        ChronologicalViewVisualization v = icv.getView();


        // test that positions and visibility settings are not lost
        int v1_1X = 2;
        int v1_1Y = 1;
        int i1X = 5;
        int i1Y = 7;

        boolean v1_1Visible = true;
        boolean i1Visible = true;

        ChronologicalViewNode v1_1Node = v.getNode(null, v1_1, true, false);
        v1_1Node.setX(v1_1X);
        v1_1Node.setY(v1_1Y);
        v1_1Node.setVisible(v1_1Visible);

        ChronologicalViewNode i1Node = v.getNode(i1, null, false, false);
        i1Node.setX(i1X);
        i1Node.setY(i1Y);
        i1Node.setVisible(i1Visible);

        UpdateChronologicalView ucv = new UpdateChronologicalView(p, v);
        v = ucv.getView();

        v1_1Node = v.getNode(null, v1_1, true, false);
        i1Node = v.getNode(i1, null, false, false);

        assertEquals(v1_1X, v1_1Node.getX());
        assertEquals(v1_1Y, v1_1Node.getY());
        assertEquals(v1_1Visible, v1_1Node.isVisible());
        assertEquals(i1X, i1Node.getX());
        assertEquals(i1Y, i1Node.getY());
        assertEquals(i1Visible, i1Node.isVisible());








        // test that handles are not lost
        ChronologicalViewAssociation association = v.getAssociation(i1, null, null, v1_1);
        Handle h = new Handle();
        h.setX(3);
        h.setY(4);
        association.addHandle(h);

        ucv = new UpdateChronologicalView(p, v);
        v = ucv.getView();

        association = v.getAssociation(i1, null, null, v1_1);

        assertTrue(association.getHandles().contains(h));







        // test that a new node can be added
        Iteration i2 = new Iteration();
        i2.setName("iii1");
        i2.setStartDate(new Date(11));
        i2.setEndDate(new Date(20));
        p.addIteration(i2);

        ucv = new UpdateChronologicalView(p, v);
        v = ucv.getView();

        assertNotNull(v.getNode(i2, null, true, false));
    }
}
