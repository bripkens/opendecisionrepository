package nl.rug.search.odr.project;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import nl.rug.search.odr.BusinessException;
import nl.rug.search.odr.GenericDaoBean;
import nl.rug.search.odr.entities.Project;
import nl.rug.search.odr.entities.ProjectMember;
import nl.rug.search.odr.entities.StakeholderRole;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
@Stateless
public class ProjectBean extends GenericDaoBean<Project, Long> implements ProjectLocal {

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
        Query q = entityManager.createQuery("SELECT pm from ProjectMember pm WHERE pm.person.id = :userId AND pm.removed = false");
        q.setParameter("userId", userId);

        return q.getResultList();
    }

    @Override
    public void updateProject(Project sourceProject) {
        entityManager.merge(sourceProject);
    }

    @Override
    public boolean isMember(long userId, long projectId) {
        Query q = entityManager.createQuery("SELECT COUNT(pm)"
                + " FROM ProjectMember pm "
                + " WHERE pm.person.id = :userId AND pm.project.id = :projectId AND pm.removed = false");
        q.setParameter("userId", userId);
        q.setParameter("projectId", projectId);

        long result = (Long) q.getSingleResult();
        return result == 1;
    }

    @Override
    public void deleteProject(Project p) {
        Project pNew = getById(p.getId());

        
        Iterator<StakeholderRole> it = pNew.getStakeholderRoles().iterator();

        StringBuilder sb = new StringBuilder();

        while(it.hasNext()) {
//            sb.append("'");
            sb.append(it.next().getId().toString());
//            sb.append("'");
            
            if (it.hasNext()) {
                sb.append(",");
            }
        }

        // potential subquery that would work if there wasn't a foreign key constraint
//          Query q = entityManager.createQuery("DELETE FROM Project p LEFT JOIN p.stakeHolderRoles pit WHERE p.id = :projectId AND pit.common = FALSE");
//
        Query t = entityManager.createQuery("DELETE FROM StakeholderRole s WHERE s.id IN (".concat(sb.toString()).concat(")"));

//        System.out.println("#################### "+q.executeUpdate() + " ######################" );

           entityManager.remove(pNew);
    }

    @Override
    public Project getByName(String name) {
        if (name == null) {
            return null;
        }

        name = name.trim().toLowerCase();

        Query q = entityManager.createQuery("SELECT p FROM Project p WHERE lower(p.name) = :name");
        q.setParameter("name", name);

        try {
            return (Project) q.getSingleResult();
        } catch (NoResultException ex) {
            return null;
        }
    }
}
