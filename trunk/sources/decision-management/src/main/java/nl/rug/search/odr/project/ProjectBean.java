
package nl.rug.search.odr.project;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import nl.rug.search.odr.BusinessException;
import nl.rug.search.odr.GenericDaoBean;
import nl.rug.search.odr.entities.Project;
import nl.rug.search.odr.entities.ProjectMember;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
@Stateless
public class ProjectBean extends GenericDaoBean<Project, Long>implements ProjectLocal {
    @PersistenceContext
    private EntityManager entityManager;


    @Override
    public boolean isPersistable(Project p) {
        if (p == null || !p.isPersistable() || isUsed(p.getName())) {
            return false;
        }

        return true;
    }

    @Override
    public void createProject(Project p) {
        if (!isPersistable(p)) {
            throw new BusinessException("Can't persist project.");
        }
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


    @Override
    public List<ProjectMember> getAllProjectsFromUser(long userId) {
        Query q = entityManager.createQuery("SELECT pm from ProjectMember pm WHERE pm.person.id = :userId");
        q.setParameter("userId", userId);

        return q.getResultList();
    }

    @Override
    public void updateProject(Project sourceProject) {
        Query q = entityManager.createQuery("DELETE FROM ProjectMember pm WHERE pm.project.id = :projectId");
        q.setParameter("projectId", sourceProject.getId());

        entityManager.merge(sourceProject);
    }


}