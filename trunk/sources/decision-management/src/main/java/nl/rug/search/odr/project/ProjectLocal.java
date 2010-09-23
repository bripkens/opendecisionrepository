
package nl.rug.search.odr.project;

import java.io.Serializable;
import javax.ejb.Local;
import nl.rug.search.odr.GenericDaoLocal;
import nl.rug.search.odr.entities.Project;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
@Local
public interface ProjectLocal extends GenericDaoLocal<Project, Long>{

    void createProject(Project p);

    public boolean isUsed(String projectName);

}
