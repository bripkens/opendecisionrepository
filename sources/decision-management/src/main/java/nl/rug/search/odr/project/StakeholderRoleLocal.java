
package nl.rug.search.odr.project;

import javax.ejb.Local;
import nl.rug.search.odr.GenericDaoLocal;
import nl.rug.search.odr.entities.StakeholderRole;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
@Local
public interface StakeholderRoleLocal extends GenericDaoLocal<StakeholderRole, Long>{

    public java.util.Collection<nl.rug.search.odr.entities.StakeholderRole> getPublicRoles();

    public nl.rug.search.odr.entities.StakeholderRole getSomePublicRole();

    public void persistRole(nl.rug.search.odr.entities.StakeholderRole role);
    
}
