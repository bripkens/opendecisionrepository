
package nl.rug.search.odr;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import nl.rug.search.odr.entities.ArchitecturalDecision;
import nl.rug.search.odr.entities.Project;

/**
 *
 * @author Stefan
 */
@Stateless
public class ArchitecturalDecisionBean implements ArchitecturalDecisionLocal {
    @PersistenceContext
    private EntityManager entityManager;
    
    @Override
    public void addDecision(ArchitecturalDecision d) {
        entityManager.persist(d);
    }

    @Override
    public boolean isUsed(String decisionName, long projectId) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
