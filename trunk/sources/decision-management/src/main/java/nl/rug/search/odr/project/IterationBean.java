package nl.rug.search.odr.project;

import java.util.ArrayList;
import java.util.List;
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
public class IterationBean extends GenericDaoBean<Iteration, Long> implements IterationLocal {

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
    public boolean isIntersection(Iteration entity, long projectId) {
    long iterationId = -1;
    if(entity.getId() != null){
        System.out.println("hat die id "+ entity.getId());
        iterationId = entity.getId();
    }

        return entityManager.createNamedQuery(Iteration.NAMED_QUERY_CHECK_FOR_OVERLAPPING_DATES, Long.class).
                setParameter("startDate", entity.getStartDate()).
                setParameter("endDate", entity.getEndDate()).
                setParameter("pid", projectId).
                setParameter("iterationId", iterationId).
                getSingleResult() != 0;
    }
}