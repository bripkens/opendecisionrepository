package nl.rug.search.odr.project;

import java.util.List;
import nl.rug.search.odr.GenericDaoBean;
import nl.rug.search.odr.entities.ProjectMember;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Stefan
 */
@Stateless
public class ProjectMemberBean extends GenericDaoBean<ProjectMember, Long> implements ProjectMemberLocal {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public boolean isPersistable(ProjectMember entity) {
        return entity.isPersistable();
    }

    @Override
    public List<ProjectMember> getAllMembersFromProject(long projectId) {
        return entityManager.createNamedQuery(ProjectMember.NAMED_QUERY_GET_ALL_MEMBERS_FROM_PROJECT).
                setParameter("projectId", projectId).
                getResultList();

    }
}
