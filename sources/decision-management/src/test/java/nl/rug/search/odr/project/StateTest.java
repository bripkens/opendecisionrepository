
package nl.rug.search.odr.project;

import nl.rug.search.odr.AbstractEjbTest;
import nl.rug.search.odr.entities.State;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
public class StateTest extends AbstractEjbTest {

    private StateLocal sl;

    @Before
    public void setUp() {
        sl = lookUp(StateBean.class, StateLocal.class);
    }

    @Test
    public void testGetInitialState() {
        State commonInitialState = new State();
        commonInitialState.setActionName("Action name");
        commonInitialState.setStatusName("Status name");
        commonInitialState.setInitialState(true);
        commonInitialState.setCommon(true);
        sl.persist(commonInitialState);

        State projectSpecificInitialState = new State();
        projectSpecificInitialState.setActionName("Action name");
        projectSpecificInitialState.setStatusName("Status name");
        projectSpecificInitialState.setInitialState(true);
        projectSpecificInitialState.setCommon(false);
        sl.persist(projectSpecificInitialState);

        State someOtherState = new State();
        someOtherState.setActionName("Action name");
        someOtherState.setStatusName("Status name");
        someOtherState.setInitialState(false);
        someOtherState.setCommon(false);
        sl.persist(someOtherState);

        assertEquals(commonInitialState, sl.getInitialState());
    }
}
