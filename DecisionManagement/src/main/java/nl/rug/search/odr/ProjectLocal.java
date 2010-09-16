
package nl.rug.search.odr;

import javax.ejb.Local;
import nl.rug.search.odr.entities.Project;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
@Local
public interface ProjectLocal {

    void createProject(Project p);

    public boolean isUsed(String projectName);

}
