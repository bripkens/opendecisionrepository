package nl.rug.search.odr.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import nl.rug.search.odr.BusinessException;
import nl.rug.search.odr.StringValidator;

/**
 *
 * @author Stefan
 * @modified Ben
 */
@NamedQueries(value = {
    @NamedQuery(name = "State.getInitialState",
                query = "SELECT s FROM VersionState s WHERE s.common = TRUE AND s.initialState = TRUE")
})
@Entity(name = "VersionState")
public class State extends BaseEntity<State> {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String statusName;

    private String actionName;

    private boolean common;

    private boolean initialState;




    public void setCommon(boolean common) {
        this.common = common;
    }




    public boolean isCommon() {
        return common;
    }




    public String getActionName() {
        return actionName;
    }




    public void setActionName(String actionName) {
        StringValidator.isValid(actionName);
        this.actionName = actionName;
    }




    public boolean isInitialState() {
        return initialState;
    }




    public void setInitialState(boolean initialState) {
        this.initialState = initialState;
    }




    public String getStatusName() {
        return statusName;
    }




    public void setStatusName(String name) {
        StringValidator.isValid(name);
        this.statusName = name;
    }




    @Override
    protected Object[] getCompareData() {
        return new Object[]{statusName, actionName, common, initialState};
    }




    @Override
    public Long getId() {
        return id;
    }




    @Override
    public void setId(Long id) {
        if (id == null) {
            throw new BusinessException("Id is null.");
        }
        this.id = id;
    }




    @Override
    public boolean isPersistable() {
        return statusName != null && actionName != null;
    }
}
