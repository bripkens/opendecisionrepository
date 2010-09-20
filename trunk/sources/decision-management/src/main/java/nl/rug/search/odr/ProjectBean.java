
package nl.rug.search.odr;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import nl.rug.search.odr.entities.Project;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
@Stateless
public class ProjectBean implements ProjectLocal {
    @PersistenceContext
    private EntityManager entityManager;
    
    @Override
    public void createProject(Project p) {
        entityManager.persist(p);
    }

    @Override
    public boolean isUsed(String projectName) {
        projectName = projectName.trim().toLowerCase();

        Query q = entityManager.createQuery("SELECT COUNT(p) FROM Project p WHERE LOWER(p.name) = :name");
        q.setParameter("name", projectName);

        long result = (Long) q.getSingleResult();
        return result != 0;
    }
}
