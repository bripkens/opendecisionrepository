
package nl.rug.search.odr.project;

import javax.ejb.Local;
import nl.rug.search.odr.GenericDaoLocal;
import nl.rug.search.odr.entities.RelationshipType;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
@Local
public interface RelationshipTypeLocal extends GenericDaoLocal<RelationshipType, Long>{




    public java.util.List<nl.rug.search.odr.entities.RelationshipType> getPublicTypes();
    
}
