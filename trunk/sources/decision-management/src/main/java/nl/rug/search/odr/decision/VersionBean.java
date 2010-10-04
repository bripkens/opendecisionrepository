package nl.rug.search.odr.decision;

import java.util.Collection;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import nl.rug.search.odr.BusinessException;
import nl.rug.search.odr.GenericDaoBean;
import nl.rug.search.odr.entities.Version;

/**
 *
 * @author Stefan
 */
@Stateless
public class VersionBean extends GenericDaoBean<Version, Long> implements VersionLocal {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public boolean isPersistable(Version entity) {
        return true;
    }

    @Override
    public void persistVersion(Version v) {
        entityManager.persist(v);
    }

    @Override
    public void updateVersion(Version sourceVersion) {
//        makeTransient(sourceVersion);
        entityManager.merge(sourceVersion);

    }
}
