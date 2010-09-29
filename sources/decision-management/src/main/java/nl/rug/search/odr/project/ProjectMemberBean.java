package nl.rug.search.odr.project;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import nl.rug.search.odr.GenericDaoBean;
import nl.rug.search.odr.entities.ProjectMember;

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
        return false;
    }

    @Override
    public List<ProjectMember> getAllMembersFromProject(long projectId) {
        Query q = entityManager.createQuery("Select pm from ProjectMember pm where pm.project.id = :projectId");
        q.setParameter("projectId", projectId);

        return q.getResultList();
    }


        // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
}
