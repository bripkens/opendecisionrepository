
package nl.rug.search.odr.decision;

import nl.rug.search.odr.AbstractEjbTest;
import nl.rug.search.odr.entities.Decision;
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

    @Before
    public void setUp() {
        dl = lookUp(DecisionBean.class, DecisionLocal.class);
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
}
