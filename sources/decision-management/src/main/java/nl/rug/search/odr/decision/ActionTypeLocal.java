
package nl.rug.search.odr.decision;

import nl.rug.search.odr.GenericDaoLocal;
import nl.rug.search.odr.entities.ActionType;

/**
 *
 * @author Stefan
 */
public interface ActionTypeLocal  extends GenericDaoLocal<ActionType, Long>  {
    
    void persistActionType(ActionType d);

}
