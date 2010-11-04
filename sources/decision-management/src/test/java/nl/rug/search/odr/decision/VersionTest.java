
package nl.rug.search.odr.decision;

import nl.rug.search.odr.entities.Decision;
import java.util.Date;
import nl.rug.search.odr.AbstractEjbTest;
import nl.rug.search.odr.entities.ProjectMember;
import nl.rug.search.odr.entities.State;
import nl.rug.search.odr.entities.Version;
import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;
/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
public class VersionTest extends AbstractEjbTest {

    private DecisionLocal dl;
    private VersionLocal vl;

    @Before
    public void setUp() {
        vl = lookUp(VersionBean.class, VersionLocal.class);
        dl = lookUp(DecisionBean.class, DecisionLocal.class);
    }
}
