package nl.rug.search.odr.project;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import nl.rug.search.odr.BusinessException;
import nl.rug.search.odr.GenericDaoBean;
import nl.rug.search.odr.entities.Iteration;

import nl.rug.search.odr.entities.Project;

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

        return entityManager.createNamedQuery(Iteration.NAMED_QUERY_CHECK_FOR_OVERLAPPING_DATES, Long.class).
                setParameter("startDate", entity.getStartDate()).
                setParameter("endDate", entity.getEndDate()).
                getSingleResult() == 0;
    }


}
