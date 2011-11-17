package nl.rug.search.odr.project;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import nl.rug.search.odr.GenericDaoBean;
import nl.rug.search.odr.entities.Project;
import nl.rug.search.odr.entities.ProjectMember;

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
    public boolean isUsed(String projectName) {
        return entityManager
                .createNamedQuery(Project.NAMED_QUERY_IS_NAME_USED, Long.class)
                .setParameter("name", projectName.trim().toLowerCase())
                .getSingleResult() != 0;
    }




    @Override
    public List<ProjectMember> getAllProjectsFromUser(long userId) {
        return entityManager
                .createNamedQuery(Project.NAMED_QUERY_GET_ALL_PROJECTS_FROM_USER)
                .setParameter("userId", userId)
                .getResultList();
    }







    @Override
    public boolean isMember(long userId, long projectId) {
        return entityManager
                .createNamedQuery(Project.NAMED_QUERY_IS_MEMBER, Long.class).
                setParameter("userId", userId).
                setParameter("projectId", projectId).
                getSingleResult() == 1;
    }




    @Override
    public Project getByName(String name) {
        if (name == null) {
            return null;
        }

        try {
            return entityManager
                    .createNamedQuery(Project.NAMED_QUERY_GET_BY_NAME, Project.class)
                    .setParameter("name", name.trim().toLowerCase())
                    .getSingleResult();
        } catch (NoResultException ex) {
            return null;
        }
    }
}
