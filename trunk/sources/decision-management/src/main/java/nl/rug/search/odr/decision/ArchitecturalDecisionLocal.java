
package nl.rug.search.odr.decision;

import nl.rug.search.odr.GenericDaoLocal;
import nl.rug.search.odr.entities.Decision;

/**
 *
 * @author Stefan
 */
public interface ArchitecturalDecisionLocal  extends GenericDaoLocal<Decision, Long>  {
    
    void persistDecision(Decision d);

    public boolean isUsed(String decisionName, long projectId);

}
