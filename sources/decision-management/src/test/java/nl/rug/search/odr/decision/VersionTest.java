
package nl.rug.search.odr.decision;

import nl.rug.search.odr.AbstractEjbTest;
import org.junit.Before;
import org.junit.Ignore;
/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
@Ignore
public class VersionTest extends AbstractEjbTest {

    private DecisionLocal dl;
    private VersionLocal vl;

    @Before
    public void setUp() {
        vl = lookUp(VersionBean.class, VersionLocal.class);
        dl = lookUp(DecisionBean.class, DecisionLocal.class);
    }
}
