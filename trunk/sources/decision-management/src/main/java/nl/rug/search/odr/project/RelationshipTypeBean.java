package nl.rug.search.odr.project;

import javax.ejb.Stateless;
import nl.rug.search.odr.GenericDaoBean;
import nl.rug.search.odr.entities.RelationshipType;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
@Stateless
public class RelationshipTypeBean extends GenericDaoBean<RelationshipType, Long> implements RelationshipTypeBeanLocal {

    @Override
    public boolean isPersistable(RelationshipType entity) {
        return entity.isPersistable();
    }
}
