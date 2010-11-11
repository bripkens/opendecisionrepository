package nl.rug.search.odr.project;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import nl.rug.search.odr.GenericDaoBean;
import nl.rug.search.odr.entities.RelationshipType;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
@Stateless
public class RelationshipTypeBean extends GenericDaoBean<RelationshipType, Long> implements RelationshipTypeLocal {

    @PersistenceContext
    private EntityManager manager;




    @Override
    public boolean isPersistable(RelationshipType entity) {
        return entity.isPersistable();
    }




    @Override
    public List<RelationshipType> getPublicTypes() {
        return manager.createNamedQuery(RelationshipType.NAMED_QUERY_GET_PUBLIC_TYPES).
                getResultList();
    }
}
