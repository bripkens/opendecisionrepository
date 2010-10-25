package nl.rug.search.odr.project;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import nl.rug.search.odr.GenericDaoBean;
import nl.rug.search.odr.entities.State;

/**
 * 
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
@Stateless
public class StateBean extends GenericDaoBean<State, Long> implements StateLocal {

    @PersistenceContext
    private EntityManager manager;




    @Override
    public String getEntityName() {
        return "VersionState";
    }




    @Override
    public State getInitialState() {
        return manager.createNamedQuery("State.getInitialState", State.class).
                setMaxResults(1).
                getSingleResult();
    }




    @Override
    public boolean isPersistable(State entity) {
        return entity.isPersistable();
    }
}
