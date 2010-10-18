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
        } else {
            Query q = entityManager.createQuery("SELECT COUNT(i) FROM Iteration i WHERE :startDate BETWEEN i.startDate AND i.endDate "
                    + "OR :endDate BETWEEN i.startDate AND i.endDate OR i.startDate BETWEEN :startDate AND :endDate "
                    + "OR i.endDate BETWEEN :startDate AND :endDate");
            q.setParameter("startDate", entity.getStartDate());
            q.setParameter("endDate", entity.getEndDate());

            long result = (Long) q.getSingleResult();
            return result == 0;
        }
    }

    @Override
    public void updateIteration(Iteration sourceIteration) {
        entityManager.merge(sourceIteration);
    }

//    private void setEndDate(Project pro) {
//        for (Iteration each : pro.getIterations()) {
//            if (each.getEndDate() == null) {
//                each.setEndDate(new Date());
//                entityManager.merge(each);
//                return;
//            }
//        }
//    }

    @Override
    public void addIteration(Project pr, Iteration i) {
        if (!isPersistable(i)) {
            throw new BusinessException("Can't persist Iteration.");
        }
        pr.addIteration(i);
        entityManager.merge(pr);

    }

    @Override
    public boolean deleteIteration(long id) {
        Query q = entityManager.createQuery("DELETE FROM Iteration i WHERE i.id = :id");
        q.setParameter("id", id);
        if (q.executeUpdate() > 0) {
            return true;
        }
        return false;
    }
}
