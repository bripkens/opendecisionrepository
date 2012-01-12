package nl.rug.search.odr.decision;

import nl.rug.search.odr.GenericDaoBean;
import nl.rug.search.odr.entities.Version;

import javax.ejb.Stateless;

/**
 *
 * @author Stefan
 */
@Stateless
public class VersionBean extends GenericDaoBean<Version, Long> implements VersionLocal {

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
