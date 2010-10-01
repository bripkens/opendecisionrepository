package nl.rug.search.odr.test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import nl.rug.search.odr.entities.BaseEntity;
import nl.rug.search.odr.entities.ProjectMember;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
@Stateless
public class TestDeleteHelper implements TestDeleteHelperLocal {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void deleteAll() {
        String[] entityNames = new String[]{"ProjectMember", "Person", "Project", "StakeholderRole",
            "Iteration", "Version", "ArchitecturalDecision", "Status", "Requirement"};

        for (int i = 0; i < 20; i++) {

            for (String entityName : entityNames) {
                Query q = entityManager.createQuery("SELECT x FROM " + entityName + " x");
                List<BaseEntity> entities = (List<BaseEntity>) q.getResultList();

                for (BaseEntity entity : entities) {
                    try {
                        q = entityManager.createQuery("DELETE FROM " + entityName + " x WHERE x.id = :id");
                        q.setParameter("id", entity.getId());
                        q.executeUpdate();
                    } catch (Exception e) {
                    }
                }

            }
//
//            try {
//                Query q = entityManager.createQuery("DELETE FROM ProjectMember");
//                q.executeUpdate();
//                System.out.println(q.getResultList().size());
//            } catch (Exception e) {
//            }
//
//            try {
//                Query q = entityManager.createQuery("DELETE FROM Person");
//                q.executeUpdate();
//                System.out.println(q.getResultList().size());
//            } catch (Exception e) {
//            }
//
//            try {
//                Query q = entityManager.createQuery("DELETE FROM Project");
//                q.executeUpdate();
//                System.out.println(q.getResultList().size());
//            } catch (Exception e) {
//            }
//
//            try {
//                Query q = entityManager.createQuery("DELETE FROM StakeholderRole");
//                q.executeUpdate();
//                System.out.println(q.getResultList().size());
//            } catch (Exception e) {
//            }
//            try {
//                Query q = entityManager.createQuery("DELETE FROM Iteration");
//                q.executeUpdate();
//                System.out.println(q.getResultList().size());
//            } catch (Exception e) {
//            }
//            try {
//                Query q = entityManager.createQuery("DELETE FROM Status");
//                q.executeUpdate();
//                System.out.println(q.getResultList().size());
//            } catch (Exception e) {
//            }
//            try {
//                Query q = entityManager.createQuery("DELETE FROM Version");
//                q.executeUpdate();
//                System.out.println(q.getResultList().size());
//            } catch (Exception e) {
//            }
//            try {
//                Query q = entityManager.createQuery("DELETE FROM Requirement");
//                q.executeUpdate();
//                System.out.println(q.getResultList().size());
//            } catch (Exception e) {
//            }
//            try {
//                Query q = entityManager.createQuery("DELETE FROM ArchitecturalDecision");
//                q.executeUpdate();
//                System.out.println(q.getResultList().size());
//            } catch (Exception e) {
//            }
        }
    }
}
