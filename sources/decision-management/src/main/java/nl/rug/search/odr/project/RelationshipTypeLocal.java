package nl.rug.search.odr.project;

import java.util.List;
import javax.ejb.Local;
import nl.rug.search.odr.GenericDaoLocal;
import nl.rug.search.odr.entities.RelationshipType;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
@Local
public interface RelationshipTypeLocal extends GenericDaoLocal<RelationshipType, Long> {

    List<RelationshipType> getPublicTypes();
    
    boolean isUsed(String projectName);
    
    RelationshipType getByName(String relationshipType);
    
    RelationshipType getByNameOrNull(String relationship);
}
