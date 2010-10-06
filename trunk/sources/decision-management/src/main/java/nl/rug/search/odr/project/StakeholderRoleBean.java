package nl.rug.search.odr.project;

import java.util.ArrayList;
import java.util.Collection;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import nl.rug.search.odr.BusinessException;
import nl.rug.search.odr.GenericDaoBean;
import nl.rug.search.odr.entities.Project;
import nl.rug.search.odr.entities.StakeholderRole;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
@Stateless
public class StakeholderRoleBean extends GenericDaoBean<StakeholderRole, Long> implements StakeholderRoleLocal {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void persistRole(StakeholderRole role) {
        if (!role.isPersistable()) {
            throw new BusinessException("Stakeholder role is not persistable.");
        }

        entityManager.persist(role);
    }

    @Override
    public Collection<StakeholderRole> getPublicRoles() {
        Query q = entityManager.createQuery("SELECT sr FROM StakeholderRole sr WHERE sr.common = TRUE");


        return q.getResultList();
    }

    @Override
    public StakeholderRole getSomePublicRole() {
        Query q = entityManager.createQuery("SELECT sr FROM StakeholderRole sr WHERE sr.common = TRUE");
        q.setMaxResults(1);

        return (StakeholderRole) q.getSingleResult();
    }


    @Override
    public boolean isPersistable(StakeholderRole entity) {
        // TODO unfinished
        return false;
    }
}
