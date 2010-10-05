package nl.rug.search.odr.project;

import java.util.Date;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
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

        return true;
    }

    @Override
    public void updateIteration(Iteration sourceIteration) {

        entityManager.merge(sourceIteration);
    }

    private void setEndDate(Project pro) {
        for(Iteration each : pro.getIterations()){
            if (each.getEndDate() == null) {
                each.setEndDate(new Date());
                entityManager.merge(each);
                return;
            }
        }
//
//        if (!pro.getIterations().isEmpty()) {
//            Query q = entityManager.createQuery("UPDATE Iteration i "
//                    + "SET i.endDate = :endDate "
//                    + "WHERE i.endDate IS NULL AND i IN (:iterations)");
//
//            q.setParameter("endDate", new Date());
//            q.setParameter("iterations", pro.getIterations());
////
//           System.out.println("wurden geaendert: "+ q.executeUpdate());
//        }
    }


    @Override
    public void addIteration(Project pr, Iteration i) {
        setEndDate(pr);
        pr.addIteration(i);
        entityManager.merge(pr);
    }
}
