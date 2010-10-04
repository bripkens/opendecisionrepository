
package nl.rug.search.odr.project;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import nl.rug.search.odr.GenericDaoBean;
import nl.rug.search.odr.entities.Iteration;

/**
 *
 * @author Stefan
 */
@Stateless
public class IterationBean extends GenericDaoBean<Iteration, Long> implements IterationLocal{
    @PersistenceContext
    private EntityManager entityManager;


    @Override
    public boolean isPersistable(Iteration entity) {
         if (entity == null || !entity.isPersistable()) {
            return false;
        }

        return true;
    }

    @Override
    public void updateIteration(Iteration sourceIteration) {

        entityManager.merge(sourceIteration);
    }
}
