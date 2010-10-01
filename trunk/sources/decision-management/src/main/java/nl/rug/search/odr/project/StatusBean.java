
package nl.rug.search.odr.project;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import nl.rug.search.odr.GenericDaoBean;
import nl.rug.search.odr.entities.Status;

/**
 *
 * @author Stefan
 */
@Stateless
public class StatusBean extends GenericDaoBean<Status, Long> implements StatusLocal{
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public boolean isPersistable(Status entity) {
        return true;
    }

    @Override
    public void updateStatus(Status sourceStatus) {
        entityManager.merge(sourceStatus);
    }

    @Override
    public void persistStatus(Status status) {
        entityManager.persist(status);
    }

}
