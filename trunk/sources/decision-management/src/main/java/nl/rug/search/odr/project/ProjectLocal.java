
package nl.rug.search.odr.project;


import java.util.List;
import javax.ejb.Local;
import nl.rug.search.odr.GenericDaoLocal;
import nl.rug.search.odr.entities.Project;
import nl.rug.search.odr.entities.ProjectMember;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
@Local
public interface ProjectLocal extends GenericDaoLocal<Project, Long>{

    void createProject(Project p);

    public boolean isUsed(String projectName);

    public List<ProjectMember> getAllProjectsFromUser(long userId);

}