
package nl.rug.search.odr.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import nl.rug.search.odr.StringValidator;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
@Entity
public class StakeholderRole extends BaseEntity<StakeholderRole> {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    private boolean common;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        StringValidator.isValid(name);

        this.name = name;
    }

    public boolean isCommon() {
        return common;
    }

    public void setCommon(boolean common) {
        this.common = common;
    }

    @Override
    protected Object[] getCompareData() {
        return new Object[] {name, common};
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public boolean isPersistable() {
        return name != null;
    }
}
