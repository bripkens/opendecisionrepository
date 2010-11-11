
package nl.rug.search.odr.project;


import java.util.List;
import javax.ejb.Local;
import nl.rug.search.odr.GenericDaoLocal;
import nl.rug.search.odr.entities.Project;
import nl.rug.search.odr.entities.ProjectMember;

/**
 *
 * @author Stefan
 */
@Local
public interface ProjectMemberLocal extends GenericDaoLocal<ProjectMember, Long>{

    public List<ProjectMember> getAllMembersFromProject(long projectId);

}
