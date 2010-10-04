package nl.rug.search.odr.decision;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import nl.rug.search.odr.BusinessException;
import nl.rug.search.odr.GenericDaoBean;
import nl.rug.search.odr.entities.Action;

/**
 *
 * @author Stefan
 */
@Stateless
public class ActionBean extends GenericDaoBean<Action, Long> implements ActionLocal {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public boolean isPersistable(Action entity) {
        return true;
    }

    @Override
    public void persistAction(Action a) {
        if (!isPersistable(a)) {
            throw new BusinessException("Can't persist Action.");
        }
        entityManager.persist(a);
    }



}
