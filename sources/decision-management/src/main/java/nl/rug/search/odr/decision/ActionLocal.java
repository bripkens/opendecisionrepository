
package nl.rug.search.odr.decision;

import nl.rug.search.odr.GenericDaoLocal;
import nl.rug.search.odr.entities.Action;

/**
 *
 * @author Stefan
 */
public interface ActionLocal  extends GenericDaoLocal<Action, Long>  {
    
    void persistAction(Action a);

}
