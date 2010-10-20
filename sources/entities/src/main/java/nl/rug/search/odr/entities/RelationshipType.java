package nl.rug.search.odr.entities;

import javax.persistence.Entity;
import javax.persistence.Id;
import nl.rug.search.odr.BusinessException;
import nl.rug.search.odr.StringValidator;

/**
 *
 * @author Ben
 */
@Entity
public class RelationshipType extends BaseEntity<RelationshipType> {

    private static final long serialVersionUID = 1l;

    @Id
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
            throw new BusinessException("Id is null");
        }

        this.id = id;
    }




    public boolean isCommon() {
        return common;
    }




    public void setCommon(boolean common) {
        this.common = common;
    }




    public String getName() {
        return name;
    }




    public void setName(String name) {
        StringValidator.isValid(name);
        this.name = name;
    }




    @Override
    public boolean isPersistable() {
        return name != null;
    }




    @Override
    protected Object[] getCompareData() {
        return new Object[] {name, common};
    }
}
