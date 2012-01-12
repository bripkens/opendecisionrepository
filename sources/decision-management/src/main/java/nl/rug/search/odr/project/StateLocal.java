package nl.rug.search.odr.project;

import java.util.List;
import nl.rug.search.odr.GenericDaoLocal;
import nl.rug.search.odr.entities.State;

/**
 *
 * @author Ben
 */
public interface StateLocal extends GenericDaoLocal<State, Long> {

     State getInitialState();

     List<State> getCommonStates();

     State getByName(String state);

    State getByNameOrNull(String state);
}
