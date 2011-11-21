package nl.rug.search.odr.project;

import java.util.List;
import javax.ejb.Stateless;
import nl.rug.search.odr.GenericDaoBean;
import nl.rug.search.odr.entities.StakeholderRole;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
@Stateless
public class StakeholderRoleBean extends GenericDaoBean<StakeholderRole, Long> implements StakeholderRoleLocal {

    @Override
    public List<StakeholderRole> getPublicRoles() {
        return getEntityManager().
                createNamedQuery(StakeholderRole.NAMED_QUERY_GET_PUBLIC_ROLES).
                getResultList();
    }




    @Override
    public StakeholderRole getSomePublicRole() {
        return getEntityManager().
                createNamedQuery(StakeholderRole.NAMED_QUERY_GET_PUBLIC_ROLES, StakeholderRole.class).
                setMaxResults(1).
                getSingleResult();
    }




    @Override
    public boolean isPersistable(StakeholderRole entity) {
        return entity.isPersistable();
    }
}
