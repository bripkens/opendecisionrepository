
package nl.rug.search.odr.decision;

import javax.ejb.Stateless;
import nl.rug.search.odr.GenericDaoBean;
import nl.rug.search.odr.entities.ProjectMember;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
@Stateless
public class ProjectMemberBean extends GenericDaoBean<ProjectMember, Long>implements ProjectMemberLocal {

    @Override
    public boolean isPersistable(ProjectMember entity) {
        return false;
    }
    
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
 
}
