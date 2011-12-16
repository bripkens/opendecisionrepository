package nl.rug.search.odr.decision;

import nl.rug.search.odr.GenericDaoBean;
import nl.rug.search.odr.entities.Decision;

import javax.ejb.Stateless;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
@Stateless
public class DecisionBean extends GenericDaoBean<Decision, Long> implements DecisionLocal {

    @Override
    public boolean isPersistable(Decision entity) {
        return entity != null && entity.isPersistable();
    }

    @Override
    public void delete(Decision entity) {
        entity.remove();
        merge(entity);
    }
}
