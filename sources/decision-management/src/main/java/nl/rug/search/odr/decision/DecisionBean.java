package nl.rug.search.odr.decision;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import nl.rug.search.odr.GenericDaoBean;
import nl.rug.search.odr.entities.Decision;
import nl.rug.search.odr.entities.Version;

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
        return manager.createNamedQuery(Decision.NAMED_QUERY_IS_NAME_USED, Long.class).
                setParameter("id", projectId).
                setParameter("name", decisionName).
                getSingleResult() > 0;
    }




    @Override
    public Decision getByVersion(long versionId) {
        return manager.createNamedQuery(Decision.NAMED_QUERY_GET_BY_VERSION, Decision.class).
                setParameter("id", versionId).
                getSingleResult();
    }




    @Override
    public void delete(Decision entity) {
        entity.remove();

        merge(entity);
    }



}
