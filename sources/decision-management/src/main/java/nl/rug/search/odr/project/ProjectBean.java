package nl.rug.search.odr.project;

import java.util.List;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import nl.rug.search.odr.GenericDaoBean;
import nl.rug.search.odr.entities.Project;
import nl.rug.search.odr.entities.ProjectMember;
import nl.rug.search.odr.entities.Rating;

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
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public boolean isUsed(String projectName) {
        return entityManager.createNamedQuery(Project.NAMED_QUERY_IS_NAME_USED, Long.class).setParameter("name", projectName.trim().toLowerCase()).getSingleResult() != 0;
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public List<ProjectMember> getAllProjectsFromUser(long userId) {
        return entityManager.createNamedQuery(Project.NAMED_QUERY_GET_ALL_PROJECTS_FROM_USER).setParameter("userId", userId).getResultList();
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public boolean isMember(long userId, long projectId) {
        return entityManager.createNamedQuery(Project.NAMED_QUERY_IS_MEMBER, Long.class).
                setParameter("userId", userId).
                setParameter("projectId", projectId).
                getSingleResult() == 1;
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public Project getByName(String name) {
        if (name == null) {
            return null;
        }

        try {
            return entityManager.createNamedQuery(Project.NAMED_QUERY_GET_BY_NAME, Project.class).setParameter("name", name.trim().toLowerCase()).getSingleResult();
        } catch (NoResultException ex) {
            return null;
        }
    }
    
    @Override
    public void addRating(long projectId, Rating rating) {
        Project project = getById(projectId);
        
        for (Rating eachRating : project.getRatings()) {
            if (eachRating.getConcern().equals(rating.getConcern()) && 
                    eachRating.getDecision().equals(rating.getDecision())) {
                eachRating.setEffect(rating.getEffect());
                rating.setId(eachRating.getId());
                return;
            }
        }
        
        entityManager.persist(rating);
        Project p = getById(projectId);
        p.addRating(rating);
    }
}
