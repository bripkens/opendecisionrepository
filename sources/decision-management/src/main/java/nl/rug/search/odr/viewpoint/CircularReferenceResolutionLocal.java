
package nl.rug.search.odr.viewpoint;

import javax.ejb.Local;
import nl.rug.search.odr.entities.Project;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
@Local
public interface CircularReferenceResolutionLocal {

    CircularReferenceResolution dissolveCircularRelationships(Project p);

}
