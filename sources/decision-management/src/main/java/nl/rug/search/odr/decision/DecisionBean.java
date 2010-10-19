package nl.rug.search.odr.decision;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import nl.rug.search.odr.GenericDaoBean;
import nl.rug.search.odr.entities.Decision;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
@Stateless
public class DecisionBean extends GenericDaoBean<Decision, Long> implements DecisionLocal {

    @PersistenceContext
    private EntityManager manager;




    @Override
    public boolean isPersistable(Decision entity) {
        return entity != null && entity.isPersistable();
    }




    @Override
    public boolean isNameUsed(String decisionName, long projectId) {
        return manager.createNamedQuery("Decision.isNameUsed", Long.class).
                setParameter("id", projectId).
                setParameter("name", decisionName).
                getSingleResult() > 0;
    }
}
