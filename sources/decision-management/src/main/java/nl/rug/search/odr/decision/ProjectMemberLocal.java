
package nl.rug.search.odr.decision;

import javax.ejb.Local;
import nl.rug.search.odr.GenericDaoLocal;
import nl.rug.search.odr.entities.ProjectMember;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
@Local
public interface ProjectMemberLocal extends GenericDaoLocal<ProjectMember, Long> {
    
}
