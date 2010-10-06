package nl.rug.search.odr.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import nl.rug.search.odr.BusinessException;
import nl.rug.search.odr.StringValidator;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
@Entity
public class StakeholderRole extends AbstractUserTyp<StakeholderRole> {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;

    private static final long serialVersionUID = 1L;
   
    @Column
    private String name;
    @Column
    private boolean common;

    @Override
    public Long getId(){
        return id;
    }

    @Override
    public void setId(Long id){
        if(id == null){
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
        if (!StringValidator.isValid(name)) {
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
        return name != null;
    }
}
