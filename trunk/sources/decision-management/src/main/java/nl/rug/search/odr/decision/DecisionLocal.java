package nl.rug.search.odr.decision;

import javax.ejb.Local;
import nl.rug.search.odr.GenericDaoLocal;
import nl.rug.search.odr.entities.Decision;

/**
 *
 * @author Stefan
 */
@Local
public interface DecisionLocal extends GenericDaoLocal<Decision, Long> {
    Decision getByIdRefreshed(long id);
}
