
package nl.rug.search.odr.decision;

import nl.rug.search.odr.entities.ArchitecturalDecision;

/**
 *
 * @author Stefan
 */
public interface ArchitecturalDecisionLocal {
    
    void persistDecision(ArchitecturalDecision d);

    public boolean isUsed(String decisionName, long projectId);

}
