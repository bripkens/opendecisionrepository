
package nl.rug.search.odr.project;

import java.util.ArrayList;
import java.util.Collection;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import nl.rug.search.odr.entities.StakeholderRole;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
@Stateless
public class StakeholderRoleBean implements StakeholderRoleLocal {
    
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Collection<StakeholderRole> getRoles() {
        Collection<StakeholderRole> roles = new ArrayList<StakeholderRole>();

        StakeholderRole role = new StakeholderRole();
        role.setName("Architect");
        roles.add(role);

        role = new StakeholderRole();
        role.setName("Manager");
        roles.add(role);

        role = new StakeholderRole();
        role.setName("Customer");
        roles.add(role);

        return roles;
    }
}
