package nl.rug.search.odr.project;

import nl.rug.search.odr.GenericDaoLocal;
import nl.rug.search.odr.entities.Status;

/**
 *
 * @author Stefan
 */
public interface StatusLocal extends GenericDaoLocal<Status, Long> {

    public void updateStatus(Status sourceStatus);

    public void persistStatus(Status status);
}
