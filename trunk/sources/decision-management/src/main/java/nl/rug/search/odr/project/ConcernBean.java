package nl.rug.search.odr.project;

import java.util.Collection;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import nl.rug.search.odr.GenericDaoBean;
import nl.rug.search.odr.entities.Concern;

/**
 *
 * @author Stefan
 */
@Stateless
public class ConcernBean extends GenericDaoBean<Concern, Long> implements ConcernLocal {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public boolean isPersistable(Concern entity) {
        return entity.isPersistable();
    }

    @Override
    public Collection<String> getPossibleStrings(String startswith){

         return entityManager.createNamedQuery(Concern.NAMED_QUERY_FIND_SIMILAR_TAGS).
                setParameter("keyword", "%"+startswith+"%").
                getResultList();
    }
}
