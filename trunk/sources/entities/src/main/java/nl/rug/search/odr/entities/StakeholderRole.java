package nl.rug.search.odr.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import nl.rug.search.odr.BusinessException;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
@Entity
public class StakeholderRole extends AbstractUserTyp {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private boolean common;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        if (id == null) {
            throw new BusinessException("Please provide an id");
        }
        this.id = id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        if (name == null) {
            throw new BusinessException("Please provide a name");
        }
        this.name = name;
    }

    @Override
    public boolean isCommon() {
        return common;
    }

    @Override
    public void setCommon(boolean common) {
        this.common = common;
    }

    @Override
    protected Object[] getCompareData() {
       return new Object[]{name, common};
    }

    @Override
    public boolean isPersistable() {
        if(name.isEmpty()){
            return false;
        }
        return true;
    }
}
