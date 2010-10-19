package nl.rug.search.odr.project;

import nl.rug.search.odr.GenericDaoLocal;
import nl.rug.search.odr.entities.State;

/**
 *
 * @author Stefan
 */
public interface StatusLocal extends GenericDaoLocal<State, Long> {

    public void updateStatus(State sourceStatus);

    public void persistStatus(State status);
}
