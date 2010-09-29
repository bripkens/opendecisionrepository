package nl.rug.search.odr.test;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

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
        for (int i = 0; i < 10; i++) {
            try {
                Query q = entityManager.createQuery("DELETE FROM Person");
                q.executeUpdate();
            } catch (Exception e) {
            }

            try {
                Query q = entityManager.createQuery("DELETE FROM Project");
                q.executeUpdate();
            } catch (Exception e) {
            }

            try {
                Query q = entityManager.createQuery("DELETE FROM ProjectMember");
                q.executeUpdate();
            } catch (Exception e) {
            }

            try {
                Query q = entityManager.createQuery("DELETE FROM StakeholderRole");
                q.executeUpdate();
            } catch (Exception e) {
            }
        }
    }
}