package nl.rug.search.odr.project;

import java.util.Collection;
import nl.rug.search.odr.GenericDaoLocal;
import nl.rug.search.odr.entities.Concern;

/**
 *
 * @author Ben
 */
public interface ConcernLocal extends GenericDaoLocal<Concern, Long> {

    public Collection<String> getPossibleStrings(String startswith);

}
