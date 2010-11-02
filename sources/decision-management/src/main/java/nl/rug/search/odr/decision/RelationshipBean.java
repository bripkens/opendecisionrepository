package nl.rug.search.odr.decision;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import nl.rug.search.odr.GenericDaoBean;
import nl.rug.search.odr.entities.Relationship;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
@Stateless
public class RelationshipBean extends GenericDaoBean<Relationship, Long> implements RelationshipBeanLocal {



    @Override
    public boolean isPersistable(Relationship entity) {
        return entity.isPersistable();
    }
}
