
package nl.rug.search.odr.decision;

import java.util.Collection;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import nl.rug.search.odr.GenericDaoBean;
import nl.rug.search.odr.entities.Decision;

/**
 *
 * @author Stefan
 */
@Stateless
public class ArchitecturalDecisionBean extends GenericDaoBean<Decision, Long> implements ArchitecturalDecisionLocal {
    @PersistenceContext
    private EntityManager entityManager;
    
    @Override
    public void persistDecision(Decision d) {
        entityManager.persist(d);
    }

    @Override
    public boolean isUsed(String decisionName, long projectId) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Collection<Decision> getDecisions(long projectId){
        Query q = entityManager.createQuery("Select ad "
                                          + "from ARCHITECTURALDECISION ad "
                                          + "where ad.id in( Select v.id "
                                                          + "from Version v "
                                                          + "where v in (Select it.version "
                                                                      + "from Iteration it "
                                                                      + "where it in ( Select o.iterations "
                                                                                    + "from project p"
                                                                                    + "where p.id = :projectId)))");

        q.setParameter("projectId", projectId);

        return q.getResultList();
    }

    @Override
    public boolean isPersistable(Decision entity) {
        return true;
    }
}
