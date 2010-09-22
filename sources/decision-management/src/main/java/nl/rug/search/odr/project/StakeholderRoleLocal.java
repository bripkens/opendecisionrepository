
package nl.rug.search.odr.project;

import javax.ejb.Local;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
@Local
public interface StakeholderRoleLocal {

    public java.util.Collection<nl.rug.search.odr.entities.StakeholderRole> getRoles();
    
}
