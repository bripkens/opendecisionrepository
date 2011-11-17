package nl.rug.search.odr.decision;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import nl.rug.search.odr.GenericDaoBean;
import nl.rug.search.odr.entities.Version;

/**
 *
 * @author Stefan
 */
@Stateless
public class VersionBean extends GenericDaoBean<Version, Long> implements VersionLocal {

    @PersistenceContext
    private EntityManager manager;

    @Override
    public boolean isPersistable(Version entity) {
        return entity.isPersistable();
    }




    @Override
    public void delete(Version entity) {
        entity.setRemoved(true);
        merge(entity);
    }



}
