package nl.rug.search.odr.decision;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import nl.rug.search.odr.BusinessException;
import nl.rug.search.odr.GenericDaoBean;
import nl.rug.search.odr.entities.ActionType;

/**
 *
 * @author Stefan
 */
@Stateless
public class ActionTypeBean extends GenericDaoBean<ActionType, Long> implements ActionTypeLocal {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public boolean isPersistable(ActionType entity) {
        return entity.isPersistable();
    }

    @Override
    public void persistActionType(ActionType d) {
        if (!isPersistable(d)) {
            throw new BusinessException("Can't persist ActionType.");
        }
        entityManager.persist(d);
    }
}
