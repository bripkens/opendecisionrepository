
package nl.rug.search.odr.project;

import java.util.Collection;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
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

    @Override
    public Collection<Iteration> getAllITerationsByProjectId(long projectId){
        Query q = entityManager.createQuery("Select it from Iteration it where it.project.id = :projectId");
        q.setParameter("projectId", projectId);

        return q.getResultList();
    }
}
