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
        // can't use EntityManager.createNamedQuery#(String, Class<T>) due to a bug in eclipselink
        // https://bugs.eclipse.org/bugs/show_bug.cgi?id=300412
        return (State) manager.createNamedQuery("State.getInitialState").
                setMaxResults(1).
                getSingleResult();
    }




    @Override
    public boolean isPersistable(State entity) {
        return entity.isPersistable();
    }
}
