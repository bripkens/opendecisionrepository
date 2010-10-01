
package nl.rug.search.odr.decision;

import java.util.Collection;
import nl.rug.search.odr.GenericDaoLocal;
import nl.rug.search.odr.entities.Version;

/**
 *
 * @author Stefan
 */
public interface VersionLocal extends GenericDaoLocal<Version, Long> {
    
    void persistVersion(Version v);
    
    public void updateVersion(Version sourceVersion);
}
