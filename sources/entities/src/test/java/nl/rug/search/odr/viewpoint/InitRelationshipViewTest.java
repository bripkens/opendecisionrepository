package nl.rug.search.odr.viewpoint;

import nl.rug.search.odr.entities.Relationship;
import java.util.Date;
import nl.rug.search.odr.entities.Decision;
import nl.rug.search.odr.entities.Project;
import nl.rug.search.odr.entities.Version;
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










        InitRelationshipView irv = new InitRelationshipView(type, project);
        Visualization v = irv.getView();

        assertSame(project, irv.getProject());
        assertSame(type, irv.getType());

        assertFalse(v.containsVersion(v11));
        assertTrue(v.containsVersion(v12));
        assertFalse(v.containsVersion(v21));
        assertTrue(v.containsVersion(v22));
        assertFalse(v.containsVersion(removedVersion));

        assertFalse(containsRelationship(v, r));
        assertTrue(containsRelationship(v, r2));
    }




    private boolean containsRelationship(Visualization v, Relationship r) {
        for (Association a : v.getAssociations()) {
            if(a.getRelationship() == r) {
                return true;
            }
        }

        return false;
    }
}
