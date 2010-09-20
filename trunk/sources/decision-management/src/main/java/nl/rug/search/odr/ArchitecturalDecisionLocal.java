
package nl.rug.search.odr;

import nl.rug.search.odr.entities.ArchitecturalDecision;

/**
 *
 * @author Stefan
 */
public interface ArchitecturalDecisionLocal {
    
    void addDecision(ArchitecturalDecision d);

    public boolean isUsed(String decisionName, long projectId);

}
