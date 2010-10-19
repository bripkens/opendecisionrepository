
package nl.rug.search.odr.project;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import nl.rug.search.odr.GenericDaoBean;
import nl.rug.search.odr.entities.State;

/**
 *
 * @author Stefan
 */
@Stateless
public class StatusBean extends GenericDaoBean<State, Long> implements StatusLocal{
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public boolean isPersistable(State entity) {
        return true;
    }

    @Override
    public void updateStatus(State sourceStatus) {
        entityManager.merge(sourceStatus);
    }

    @Override
    public void persistStatus(State status) {
        entityManager.persist(status);
    }

}
