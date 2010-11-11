
package nl.rug.search.odr.project;

import java.util.Collection;
import javax.ejb.Local;
import nl.rug.search.odr.GenericDaoLocal;
import nl.rug.search.odr.entities.Project;
import nl.rug.search.odr.entities.StakeholderRole;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
@Local
public interface StakeholderRoleLocal extends GenericDaoLocal<StakeholderRole, Long>{

    public Collection<StakeholderRole> getPublicRoles();

    public StakeholderRole getSomePublicRole();

}
