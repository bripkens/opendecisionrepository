package nl.rug.search.odr.project;

import nl.rug.search.odr.GenericDaoLocal;
import nl.rug.search.odr.entities.State;

/**
 *
 * @author Ben
 */
public interface StateLocal extends GenericDaoLocal<State, Long> {

    public nl.rug.search.odr.entities.State getInitialState();

    public java.util.List<nl.rug.search.odr.entities.State> getCommonStates();
}
