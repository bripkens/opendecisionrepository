
package nl.rug.search.odr.viewpoint;

import java.util.Date;
import nl.rug.search.odr.entities.Decision;
import nl.rug.search.odr.entities.Project;
import nl.rug.search.odr.entities.Version;
import org.junit.Test;
import org.junit.Before;
import static nl.rug.search.odr.Assert.*;
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
        project.addDecision(d1);
        Version v11 = new Version();
        v11.setDecidedWhen(new Date());
        d1.addVersion(v11);
        Version v12 = new Version();
        v12.setDecidedWhen(new Date(v11.getDecidedWhen().getTime() + 1));
        d1.addVersion(v12);

        Decision removedDecision = new Decision();
        project.addDecision(removedDecision);
        Version removedVersion = new Version();
        removedVersion.setRemoved(true);
        removedVersion.setDecidedWhen(new Date());
        removedDecision.addVersion(removedVersion);

        InitRelationshipView irv = new InitRelationshipView(type, project);
        Visualization v = irv.getView();

        assertTrue(v.containsVersion(v12));
    }
}
