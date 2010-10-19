
package nl.rug.search.odr.decision;

import nl.rug.search.odr.AbstractEjbTest;
import org.junit.Before;
import org.junit.Test;

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
    public void isNameUsed() {
        
    }
}
