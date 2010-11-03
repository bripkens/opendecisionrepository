package nl.rug.search.odr.project;

import java.util.List;
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
    public State getInitialState() {
        return manager.createNamedQuery(State.NAMED_QUERY_GET_INITIAL_STATE, State.class).
                setMaxResults(1).
                getSingleResult();
    }



    @Override
    public List<State> getCommonStates() {
        return manager.createNamedQuery(State.NAMED_QUERY_GET_COMMON_STATES).
                getResultList();
    }


    @Override
    public boolean isPersistable(State entity) {
        return entity.isPersistable();
    }
}
