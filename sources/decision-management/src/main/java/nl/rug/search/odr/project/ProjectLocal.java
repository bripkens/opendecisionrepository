package nl.rug.search.odr.project;

import java.util.List;
import javax.ejb.Local;
import nl.rug.search.odr.GenericDaoLocal;
import nl.rug.search.odr.entities.Project;
import nl.rug.search.odr.entities.ProjectMember;
import nl.rug.search.odr.entities.Rating;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
@Local
public interface ProjectLocal extends GenericDaoLocal<Project, Long> {

    boolean isUsed(String projectName);

    List<ProjectMember> getAllProjectsFromUser(long userId);

    boolean isMember(long userId, long projectId);

    Project getByName(java.lang.String name);
    
    void addRating(long projectId, Rating rating);
}
