package nl.rug.search.odr.decision;

import javax.ejb.Local;
import nl.rug.search.odr.GenericDaoLocal;
import nl.rug.search.odr.entities.Relationship;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
@Local
public interface RelationshipLocal extends GenericDaoLocal<Relationship, Long> {

    
}
