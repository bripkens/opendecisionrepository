
package nl.rug.search.odr.decision;

import java.util.List;
import nl.rug.search.odr.project.RelationshipTypeBean;
import nl.rug.search.odr.project.RelationshipTypeLocal;
import nl.rug.search.odr.AbstractEjbTest;
import nl.rug.search.odr.entities.Decision;
import nl.rug.search.odr.entities.Relationship;
import nl.rug.search.odr.entities.RelationshipType;
import nl.rug.search.odr.entities.Version;
import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;
/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
public class DecisionTest extends AbstractEjbTest {

    private DecisionLocal dl;
    private RelationshipTypeLocal rtl;
    private VersionLocal vl;

    @Before
    public void setUp() {
        dl = lookUp(DecisionBean.class, DecisionLocal.class);
        rtl = lookUp(RelationshipTypeBean.class, RelationshipTypeLocal.class);
        vl = lookUp(VersionBean.class, VersionLocal.class);
    }

    @Test
    public void getByVersion() {
        Decision d = new Decision();
        d.setName("Foobar");
        Version v = new Version();
        d.addVersion(v);

        dl.persist(d);

        d = dl.getById(d.getId());

        long decisionId = d.getId();

        v = d.getVersions().iterator().next();

        d = dl.getByVersion(v.getId());

        assertEquals(decisionId, (long) d.getId());
    }


    @Test
    public void testDelete() {
        Decision d = new Decision();
        d.setName("bdadsa");
        Version v = new Version();
        d.addVersion(v);
        v = new Version();
        d.addVersion(v);

        dl.persist(d);

        dl.delete(d);

        d = dl.getById(d.getId());

        assertTrue(d.isRemoved());
    }


  
}
