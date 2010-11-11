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
    public List<StakeholderRole> getPublicRoles() {
        return entityManager.
                createNamedQuery(StakeholderRole.NAMED_QUERY_GET_PUBLIC_ROLES).
                getResultList();
    }




    @Override
    public StakeholderRole getSomePublicRole() {
        return entityManager.
                createNamedQuery(StakeholderRole.NAMED_QUERY_GET_PUBLIC_ROLES, StakeholderRole.class).
                setMaxResults(1).
                getSingleResult();
    }




    @Override
    public boolean isPersistable(StakeholderRole entity) {
        return entity.isPersistable();
    }
}
