
package nl.rug.search.odr.project;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import nl.rug.search.odr.entities.Iteration;

/**
 *
 * @author Stefan
 */
@Stateless
public class IterationBean implements IterationLocal {
    @PersistenceContext
    private EntityManager entityManager;


    @Override
    public void addIteration(Iteration i) {
        entityManager.persist(i);
    }
}
