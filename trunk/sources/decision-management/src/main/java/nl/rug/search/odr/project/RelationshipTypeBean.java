package nl.rug.search.odr.project;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
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

    @Override
    public boolean isUsed(String relationshiptypeName) {
        return manager.createNamedQuery(RelationshipType.NAMED_QUERY_IS_NAME_USED, Long.class).setParameter("name", relationshiptypeName.trim().toLowerCase()).getSingleResult() != 0;
    }

    @Override
    public RelationshipType getByName(String relationship) {
        return manager.createNamedQuery(RelationshipType.NAMED_QUERY_BY_NAME, RelationshipType.class).
                setParameter("relationshipType", relationship).
                getSingleResult();
    }
    
    @Override
    public RelationshipType getByNameOrNull(String relationship) {
        try {
            return getByName(relationship);
        } catch (NoResultException ex) {
            return null;
        }
    }
}
